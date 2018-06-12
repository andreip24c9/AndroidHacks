var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var router = express.Router();

app.use(bodyParser.json());
app.use('/', router);
app.use(bodyParser.urlencoded({
  "extended": false
}));

// Access Control Allow Origin (*)
app.all('*', function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  next();
});
app.use(function(req, res, next) {
  var origin = req.headers.origin;
  if (origin != null) {
    res.setHeader('Access-Control-Allow-Origin', origin);
  }
  res.header('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization');
  res.header('Access-Control-Allow-Credentials', true);
  return next();
});

// REST endpoints
router.route("/cars/getAll")
  .get(function(req, res) {
    res.statusCode = 200;
    res.json(
      [{
          make: "Mercedes-Benz",
          model: "A 180",
          price: "13600.00",
          year: "2015",
          km: "130000",
          picture: ""
        },
        {
          make: "Renault",
          model: "Clio 1.5 dCi 8V 75CV 5 porte, Van 4 posti.",
          price: "6699.00",
          year: "2015",
          km: "82167",
          picture: "https://secure.pic.autoscout24.net/images-420x315/602/014/0340014602001.jpg?41bca897e106bd1c0e73cbd6b70de5b4"
        },
        {
          make: "BMW",
          model: "530 Serie 5 (F10/F11) xDrive 249CV Touring Msport",
          price: "6500.00",
          year: "2016",
          km: "180000",
          picture: "http://bmw-4-sale.com/images/bmw_for_sale/2565/bmw-530d-m-sport-268bhp-twin-turbo-sophistio-grey-metalic-big-spec-2565-1.jpg"
        },
        {
          make: "Ford",
          model: "Fiesta 1.25 Trend 82CV 5P",
          price: "6450.00",
          year: "2015",
          km: "46588",
          picture: "https://secure.pic.autoscout24.net/images-420x315/173/178/0326178173001.jpg?346ac80a9a9aecd586cf7a60732caf70"
        },
        {
          make: "Opel",
          model: "Corsa 1.3 CDTI Enjoy Start/Stop",
          price: "5200.00",
          year: "2015",
          km: "245000",
          picture: "https://secure.pic.autoscout24.net/images-420x315/883/098/0339098883001.jpg?649620d232904569218eda9c94d216c8"
        },
        {
          make: "Audi",
          model: "A1 1.6 TDI Sportback S tronic",
          price: "7600.00",
          year: "2017",
          km: "17000",
          picture: "https://cdn.images.autoexposure.co.uk/AETA89139/AETV48966192_1e.jpg"
        },
        {
          make: "Volkswagen",
          model: "Golf VII 7 Variant *1. HAND * EURO 6 * Temp",
          price: "8450.00",
          year: "2015",
          km: "198700",
          picture: "https://secure.pic.autoscout24.net/images-420x315/662/912/0338912662001.jpg?a926a143c8317512558c6f21ba2406c1"
        },
        {
          make: "Fiat",
          model: "Punto 1.2i Street",
          price: "6250.00",
          year: "2016",
          km: "47000",
          picture: "https://secure.pic.autoscout24.net/images-420x315/326/561/0338561326001.jpg?7e98f3c80c3f833df651ff3d7ce2740e"
        },
        {
          make: "Alfa Romeo",
          model: " MiTo 1.4 70 CV 8V",
          price: "7400.00",
          year: "2015",
          km: "49000",
          picture: "https://secure.pic.autoscout24.net/images-420x315/822/318/0340318822001.jpg?d670d4cf1d5adf67a7067602dba849e3"
        }
      ]
    );
  });

// start listening
app.listen(8080);
console.log("Listening on PORT 8080");

