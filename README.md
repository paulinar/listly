# Listly

A to-do list app for Android.

**Completed Features:**

 * [x] (Required): Add task
 * [x] (Required): Edit task
 * [x] (Required): Delete task
 * [x] (Required): Restore previously persisted items when app restarts 
 * [x] (Extra): Persist task items into SQLite instead of a text file 
 * [x] (Extra): Styled UI and custom launch screen design
 * [x] (Extra): Set completion due dates for tasks
 * [x] (Extra): Set priority levels for tasks
 * [x] (Extra): Editable notes field for attaching longer descriptions/notes per task
 * [x] (Extra): Adaptive to different screen sizes and orientations (landscape vs. portrait)

**App Structure:**

I chose to structure my app using the Model-View-Presenter (MVP) pattern to separate the presentation layer from the logic.

 * MODEL (provides data to display in the View): `Task`, `Database` 
 * VIEW (calls methods from the Presenter for interface actions): `AllTasksActivity`, `EditTaskActivity`, `TaskDetailActivity`
 * PRESENTER (retrieves data from the Model and returns it to the View): @@@@@@@@@@@@@@@

**GIF walkthrough of all required and optional stories:**

![Video Walkthrough](@@@@@@@@@@@.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
