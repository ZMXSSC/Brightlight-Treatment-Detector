# Brightlight Treatment Detector
For UC San Diego Health
***
This is a simple java-based android app that I made during the summer of 2021 while I was interning at UC San Diego Health. Light therapy is widely used to improve people's life quality. However, when we send the lightbox to the patient, we are unsure if they will take it seriously(to actually use it). So I made this app to help our doctor(s).

When an inexpensive Android smartphone is set up in front of the light treatment device (in most cases the patient will hang the phone around their neck), this app will continuously monitor whether a patient is sitting in front of the light correctly.
This app can:
1. Start recording the illuminance once clicking the "start" button.
2. Stop recording the illuminance, then generate a chart(adapted from [here](https://github.com/PhilJay/MPAndroidChart "Mikephil Android chart")) with the x-axis being time(second), y-axis being the illuminance(lux) by clicking the "stop" button,
3. Determine if a patient is following the instructions. For example, if the doctor ends up getting a flat chart from the patient, then possibly this patient is sitting in front of the lamp the entire time. However, if the doctor ends up getting an uneven chart, then possibly this patient might not be sitting in front of the lamp the entire time.

## Upcoming features that I'm working on:
1. Account system that associates with database(i.e. firebase).
2. "Auto send" in which the user will be able to send the real-time data to the database just so doctors can directly access the data.

### Further feature idea(s):
1. May associate with wearable device(s) as phone is not that portable.
