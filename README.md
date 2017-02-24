# homework-scheduler
A simple task &amp; homework task scheduler for Android. It has the main usual implementation of SQLite database inside the app consisting of insertion, deletion and modification of data. It also implements the core Android features like Alert boxes and DatePickerDialog for selecting date and time. The four main components for this application are:

1. Inserting Task:
  - User enter the task name, description and select the start and end date via the DatePickerDialog
  - The entered data were inserted to the internal database and display it in the home screen

2. Viewing All Tasks:
  - In the home screen, the user can view all the inserted task data displaying the task name and starting date
  - Clicking on the listview item displays the task description and end date

3. Modifying tasks:
  - When the user clicks on the listview item, the user can view and modify the task data.

4. Deleting tasks:
  - To delete a task, the user can select the "Delete" option from the action bar menu and confirm the deletion of data from the database
