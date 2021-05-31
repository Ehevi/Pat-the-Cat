# Pat-the-Cat
This is a simple game written with the Scala programming language and Akka Actors.

## Running an app
```
git clone https://github.com/Ehevi/Pat-the-Cat Pat-the-Cat
cd Pat-the-Cat
sbt run
sbt test
```
## Game description
Once the app starts, you'll see five kitties of different colors.

![before-start](https://user-images.githubusercontent.com/48785655/120229131-ca2a3980-c24c-11eb-96ae-ab8806391635.png)

Click on the `Start!` button to start the game. From this moment the timer counts down from 60 seconds.
Your every next click on any of the kitties will add up to your score depending on the current state of the clicked kitty:

| State | Active frame(s) | Score  |
| ------------- |-|-:|
| running | ![bieg1](https://user-images.githubusercontent.com/48785655/120229214-ed54e900-c24c-11eb-9b74-01c05752938d.gif) ![bieg2](https://user-images.githubusercontent.com/48785655/120229244-02ca1300-c24d-11eb-8331-7f61a0e70c06.gif) | + 1 |
| eye gazing |   ![pobudka](https://user-images.githubusercontent.com/48785655/120229260-09588a80-c24d-11eb-8f3d-0113e17eda17.gif) ![zatrzymanie](https://user-images.githubusercontent.com/48785655/120229270-0e1d3e80-c24d-11eb-9168-3756a05ee2f0.gif) | + 10 |
| scratching |  ![drapanie1](https://user-images.githubusercontent.com/48785655/120229282-12495c00-c24d-11eb-94be-302fe9d776f4.gif) ![drapanie2](https://user-images.githubusercontent.com/48785655/120229285-14abb600-c24d-11eb-8a2c-8d5a939e6af6.gif) | - 1 |
| yawning | ![ziewanie](https://user-images.githubusercontent.com/48785655/120229294-19706a00-c24d-11eb-893f-0d8e12e6c512.gif) | + 10 |
| sleeping |  ![spanie1](https://user-images.githubusercontent.com/48785655/120229300-1d03f100-c24d-11eb-9546-741df27ec941.gif) ![spanie2](https://user-images.githubusercontent.com/48785655/120229302-1f664b00-c24d-11eb-92a0-2daa5090bd95.gif) | - 2 |

The more points you collect from the kitty, the faster it changes its state. The speed of each kitty is independent from another ones, so your kitties will most likely end up having diffferent speed.

The game ends when the timer reaches zero or when you click on the `Stop!` button.
