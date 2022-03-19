# Brightlight Treatment Detector
For UC San Diego Health
***
This is a java-based android app that I made over the summer of 2021 while I was interning at UC San Diego Health. Light therapy is widely used to improve people's health quality. However, when we send the lightbox to the patient, we are not sure if they will take it seriously(to actually use it). So I made this app to help our doctors.

When an inexpensive Android smartphone is set up in front of the light treatment device (most of the time the patient will hang the phone around their neck), this app will continuously monitor whether a patient is sitting in front of the light correctly.
This app can:
1. Start recording the illuminance value once clicking the "start" button.
2. By clicking the "stop" button, the illuminance values recording will be terminated. Then, it will generate a chart(adapted from [here](https://github.com/PhilJay/MPAndroidChart "Mikephil Android chart")) with the x-axis being time(second), y-axis being the illuminance(lux) value.
3. Determine if a patient is following instructions. For example, if the doctor ends up getting a flat chart from the patient, then possibly this patient is sitting in front of the lightbox the entire time. However, if the doctor ends up getting an uneven chart, then possibly this patient might not be sitting in front of the lightbox the entire time.

## Upcoming features that I'm working on:
1. Account system that associates with database(i.e. firebase).
2. "Auto send" in which the user will be able to send the real-time data to the database just so doctors can directly access the data.
3. Better UI with accommodation(consider the majority of our users are patient, it's important that the UI should not be not too complicated).

### Further feature idea(s):
1. May associate with wearable device(s) as phone is not that portable while hanging.
