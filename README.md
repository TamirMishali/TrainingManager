
# Training Manager App

The Training Manager App is an Android application designed to help users create, edit, log, and track their gym workouts. Whether you’re a fitness enthusiast or a beginner, this app provides a convenient way to organize your routines and exercise sessions.

This project is a personal challenge i took in order to track my progress in the gym, have an interactive UI that shows me my performance compared to last workout, and the desire to learn and study new technologies. 


## Project Overview
The Training Manager App allows users to:

* Create personalized gym routines.
* Add workouts to their routines.
* Add customable exercises to each workout categorized by muscle group.
* Log exercises with sets, reps, and weights.

## Installation

**Option 1**
1. download and install apk file from ".\app\release\TrainingManager.apk"

**Option 2**
1. Clone this repository to your local machine:

```bash
  git clone https://github.com/TamirMishali/TrainingManager.git
```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## Usage/Examples

1. **Creating a routine:**
* Launch the app and navigate to the “Routines” section by clicking on "Edit Workouts":

<div align="center"><img src="https://github.com/TamirMishali/TrainingManager/blob/ex_db_change/Screenshots/2024-02-21/1-MainScreen.jpg?raw=true" width="300" /> </div>

* Pick the desired routine. If there are no routines, add the first one by clicking the "+" sign.
* Give your routine a name.

2. **Adding workouts to the routine:**
* Within a routine, add as much workouts you desire. For example, for a routine composed out of a cycle of 2 workouts, you can add them by pressing the "+" sign on the bottom right. The workouts can be named 'A' and 'B', or a more intuitive name such as "Back, Shoulders and Biceps" and "Chest, Legs and Triceps". 
* To edit a workout, long press it. 

3. **Adding exercises to the workout:**
* Each workout holds a list of exercises. To add an exercise, press the "+" sign ont he bottom right. This should lead you to the add/edit exercise window, where you can customize your exercise
<div align="center"><img src="https://github.com/TamirMishali/TrainingManager/blob/ex_db_change/Screenshots/2024-02-21/3-AddExercisesToWorkout-EditExScreen.jpg?raw=true" width="300" /></div>

* Choose the muscle group under "Main muscle".
* Specify the operation name under "Operation name". If it was used before, you can pick it from the auto complete list.
* If needed, choose a nickname that replaces the "Main muscle" text in the exercise final name. For example, "triceps extension" can be easly changed to "kickback extension" instead. 
* Provide further specifications if needed such as load type, position, angle, etc.
* Save.
* This will add the exercise to the list of exercises for the chosen workout:
<div align="center"><img src="https://github.com/TamirMishali/TrainingManager/blob/ex_db_change/Screenshots/2024-02-21/3-AddExercisesToWorkout-ListOfEx.jpg?raw=true" width="300" /></div>

* To edit an exercies, long press it.

4. **Workout Now!**
* After finishing setting up your routine, on the main app screen, press the first option "Start new workout now!".
* The following will open a window with all workouts available in the most recent routine, sorted by date (oldest first):
<div align="center"><img src="https://github.com/TamirMishali/TrainingManager/blob/ex_db_change/Screenshots/2024-02-21/1-WorkoutNow_ChoseWorkout.jpg?raw=true" width="300" /></div>

* Chosing the desired workout will channel you to the documentation window:
<div align="center"><img src="https://github.com/TamirMishali/TrainingManager/blob/ex_db_change/Screenshots/2024-02-21/2-WorkoutNow_ActiveWorkout.jpg?raw=true" width="300" /></div>

* For the first workout, all exercises will have 0 sets. add sets by clicking the "+" sign on the right edge of each exercise name.
## Features
**Active Workout:**
- Exercises can be collapsed and extended. 
- Of the left, the weight and reps of the previous workout (of the same type, i.g. if i do workout "A", then the data from the previous workout "A" will be presented.)
- The working set data from previouse workout can be easly duplicated to the current working set in current workout by pressing the "->" sign. 
- A working set can be deleted by pressing the trashcan sign on its left.
- Whenever all exercises sets and weight information is filled, a small "V" sign apears to ease on the user. 
- For an active workout, if not all the data slots are filled and the user exits the application, by reopening the app the workout could be easly continued from its last point by clicking the first option on the main screen which is changed to "Continue workout" instead of "start new workout now!".

**Past workouts:**
- All past routines and their workouts can be observed by clicking "Workout history" on the main screen.

**General features:**
- Deletion from DB by sweeping from left to right: 
  - In "working history": workout instances.
  - In "Edit workouts": routines, the workouts within the routines, and the exercises within the workouts. 
- The SQL database can be exported by clicking the 3 dots on the main screen, which is saved directly to the downloads folder.



## Contact information
For any question, im available:
tamirmishali@gmail.com

## Authors
- [@tamirmishali](https://www.github.com/TamirMishali)

