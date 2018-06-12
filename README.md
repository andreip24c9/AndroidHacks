# AndroidHacks

Short Description

The client-side version of the presented software is split into two main components: 
A seemingly harmless application that is meant to have some mock features that would lead the user to believe it is a legitimate program meant to help its target. 
A hidden malware that will secretly perform unauthorised activities on the device (such as spying on user activity, collecting private data, intercepting user interaction and sending unauthorized information to a remote server, etc.).

The first component of the software has the role to present some information to the end user, information that seems trustworthy and, without user interaction, silently install the second component. Having this decoupled system means that even if the victim uninstalls the first application, the malware remains installed on the targeted device.


In Depth Descrioption

3.1. Deep Malware Description
In order to get a better understanding of the system implementation, a deeper overview of the feature set should be covered at this step. To fulfil the previously set goals (Chapter 1, paragraph 1.3 ), the following steps have been taken:
The described host app used to exemplify the installation of the malware is a car selling application. In here the user can see a listing of cars, in two different forms, either a list, or a grid. Once the user clicks on one of the items he will be taken to a screen with more details regarding the possible vehicle selection. (img 3.1.1)

(3.1.1 - HostApp main screens)
Finding a way to conceal the malware within the host application was the starting point of the whole system. The option that was chosen for this step was steganography. Steganography, which essentially means hidden writing, is the concept  in which information is hidden inside files such as images, videos or documents. So for this step, the development chosen was that an image intensive application should be the base of the solution, having the malware concealed in one of them.
In order to achieve the goal of installing the malware application without the user noticing anything suspicious, several routes were taken into consideration, however only two of them remained viable options after multiple failed attempts. The main issue encountered was that whenever an application tries to install another piece of software, the Android PackageManager takes over and asks the user if such action is to be permitted. The main goal became to bypass this popup and hide information from the user.
The first route taken in this direction was to target users with root access to their phones. To understand what root access means, we have to understand that when it comes to mobile operating systems, by default, the user does not have admin rights on the device, only applications installed in the system partition (such as the default messaging application, dialer application, or carrier installed software) having the necessary privileges to make decisions of adding or removing data without asking for user permission. Having a “rooted” device, means the user has admin rights and Android being a Linux based operating system, rooting can be translated to running each instruction with “sudo”. Some users rely on this solution to either uninstall undesired applications from their handsets, access, otherwise inaccessible, OS level settings, or to install a different OS altogether, commonly known as flashing a custom ROM. This in itself can represents a major security breach, as running any application in an administrator environment means that it can request root access (with poor configuration in the middleware layer of granting permissions to such requests) and have access to installing and uninstalling programs silently. The installation of malware on rooted devices is possible, however, the target demographic can be significantly small due to the nature of gaining such root privileges, a process arguably more technical than the average consumer would approach, therefore for the purpose of this study this was not further investigated .  
The second route was exploiting the Accessibility Services built in the OS. These services provide aid for people with disabilities with different features that make using the phone easier, such as text to speech or increased haptic feedback and every developer can implement such a service inside his/her app to better suit this target. Historically, though, these services have been abused by applications that had no connection with helping disabled people, but instead served as workarounds for global actions, like swiping across the screen within any application to reveal the notification shade, or drawing a shape to start the camera, in launcher type applications. Ironically these properties (e.g. gestures) were used to implement paid extra features for premium users. Looking at the lack of regulation regarding the use of Accessibility Services a possible backdoor to achieve the goals of the experiment was identified. Having a quick look into the documentation revealed that by having access to this Android feature, an application can retrieve screen information and callbacks when applications are opened and even when screens are changed within a single application. This is to be expected, as a developer has to write some logic into clicking the buttons for the user at his/her request. This whole elaborate mechanism paved the way into solving the problem altogether, targeting even more users than with the previously stated solution. Now, when the user first enters the application, a popup appears that informs him/her of activating an Accessibility Service. The approach here was based on users’ power of habit to instinctively click “yes” on a seemingly trustworthy system like pop-ups (img 3.1.2), relying also on Android’s permission for the developers to name the service and populate its description screen freely, using for example text copied from Google’s terms of service. With this in mind, having such a running services gives the attacker the possibility of taking a screenshot of the current state of the application, run the installation process of the application (starting the above mentioned pop-up) and quickly display on top of it the screenshot of the previous phone state. The user will not notice any difference in the UI and the hacker uses this as a curtain to silently press the buttons behind it, in order to proceed with the installation. Once this step is finished, the overlay is removed from the screen, leaving the user back at the application, never knowing what actually happened, as this process finishes in a matter of seconds. 

(3.1.2 - Misleading dialogue)
Research distribution channels for the HostApp was mandatory at this stage to maximize the number of potential targets. With the history of loose regulations surrounding Accessibility Services, Google has made it difficult in the past year to have a published application on their platform, Google Play Store, that uses these services for tasks they are not intended to serve. They now inspect use of these features to ensure minimum possible damages, however, not all publishers are experiencing the same evaluation from the platform, though, to minimise any possible issues at this stage, a different channel has to be chosen. Moreover, in order for this solution to work properly the user has to have a global setting enabled - “install from unknown sources” (img 3.1.3). Without this specific permission the malware will not be able to be installed anyway as it does not come from the Play Store, but instead its source is our initial app.

(3.1.3 - Android Global Security Setting)
It is important to also take into account Android modifications throughout its iterations. This global setting was present in versions of the system up until Android Nougat 7.x. From that point on, starting with Android Oreo 8.x and continuing the trend with future iterations, such as de developer preview 9.0 this setting has been moved to a different section. Now each application has the possibility to be configured to allow installation of foreign software closing this backdoor gap even more (img 3.1.4). The conclusion to be drawn here is that our HostApp (together with the malware) needs to be distributed on a different channel, other than the Play Store, and the targeted OS version should be below  Android Oreo 8.0, this ensuring the user has a global setting of unprotected installation of applications and that the setting is checked as he has to download the initial app from a different market. To maximize the target audience a virus free version of the application could be published on the Play Store as a paid service, while a free version including the malware would be distributed through alternative channels such as Amazon App store for Android, Mobogenie, F-Droid, etc 

(3.1.4 - Android Per-App Security Setting)
At the malware level, certain Android features were researched in order to do the best in hiding the virus from the end user. What was discovered is that the application icon could be hidden, this being an aspect useful for launcher type application, also, by following this trend, another interesting factor was uncovered, the ability to hide the application from the recents menu. Also, on certain versions of the Android OS services misbehaving, such as running more than it is expected of them to do, are signaled to the user in order to stop them. To prevent the operating system to detect any anomalies, a certain behavior is made possible by starting and stopping services at given times, resulting in a virus that is silent most of the times and therefore really hard to track by the user.
Having most of the issues leveled out already, this step was crucial in determining what malicious activities could be undertaken. In order to fully understand the amount of access an application has to the data stored on a user’s handset we need to talk about the android permission system. Up until version 6.0 Marshmallow of the OS, permissions for features such as accessing internal storage, device contacts or camera capabilities were requested during the installation of the application, without the ability for the user to choose which to grant. The user had two basic choices, either grant all the permissions and use the application or not install it at all (img. 3.1.5). From that specific version forward, a new system was put into place, where no permissions were asked at the installation phase, but instead, when using the desired software. If for any specific reason the application needs access to the camera, for instance, it has to ask the user with a specific dialogue (img 3.1.6). This makes possible the use of a certain program with a limited functionality needed by a specific user and also discourages developers to request more access than the app needs, having to justify each request for a permission. With this in mind, the solution for the experiment was to limit the target execution of the application to devices with 5.2 Lollipop and below, and therefore to have all the necessary permissions without informing the user. From the statistical data presented in Chapter 1 Paragraph 1.4 (January 2018 User Statistics) we can conclude that these represent more than 97% of existing Android devices as only the HostApp has to adhere to a minimum sdk, the limitation put on the malware are only for exploiting the vulnerabilities of an old framework version, being backward compatible, the vulnerabilities propagate with newer versions as well. Having access to all these features means the malware can collect personal information such as messages, emails, take pictures at certain moments, retrieve location information and remove or encrypt data already inside the phone.

(3.1.5 - Android Old Permission System)                 (3.1.6 - Android New Permission System)
As the developer of a malware might want to monetize his solution, multiple possibilities are available. A malware might be used to:
Collect and sell data from the users, such as: address books, emails and phone numbers.
Encrypt data on users’ phones and ask them to pay a certain amount of money to reclaim the lost information (“ransomware”).
With the possibility to display information as overlays on top of other applications, inspired by studying features like “chat heads” from Facebook Messenger, a passive, less intrusive, monetization system could be put into place. This works by detecting the foreground application on the victim’s device and collect information of any possible comercial displayed on it. Having this knowledge means the malware could put a banner ad on top of the one present in the application currently in use. The user would not see any abnormal behavior and for each click on the specific adware, instead of the company that designed the application in question gaining the revenue, the malware would get it instead.
Also, by following more or less the same logic mentioned above, knowing what is in use at any given time on the infected device, the malware could take screenshots of banking activities screens and pass that information along to a remote server, gaining the relevant data associated to accounts, payments, cards, etc.
