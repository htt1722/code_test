1.HOW TO RUN PROGRAM?
a. Unzip the zip file, import it into the IDEA project, and select "Maven project";
b. Install project in maven;
c. Run the main method in the class "PaymentMain".

2.ABOUT THE PROGRAM.
a. The first step is to select whether the exchange rate needs to be set.(Enter "Y" to go to step "b", enter other steps to step "c")
b. You can set the exchange rate in this format: CNY 0.8 (It's like the format of payment records).
   Input wrong format data, will prompt error message and finish the input of exchange rate, the program continues to run.
   Input "F" will normally complete the exchange rate input.
c. You can enter a specified file name to load the data (the file must be placed in the "resources" folder).
   If the program cannot find the specified file, the default file "payment_list.txt" is loaded.
d. Now, you can enter payment records from the console. You have to type in the sample format, or the program will exit automatically.
   The following formats are all wrong records: "Abc 999", " AAA 00", "AAA AAA", "ABC 1 CCC 1", etc.
   Enter "quit" to exit the program manually.
e. Every other minute, the calculation results will be automatically printed to the console.