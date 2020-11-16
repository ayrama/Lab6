import javax.swing.*;
import java.io.*;
///////////////////
class Link{
    public int studentID;
    public double studentTotalPoints;
    public double maxTotalPoints;
    public String grade;
    public Link next;
    //------------------------------------------------
    public Link(int studentID, double studentTotalPoints, double maxTotalPoints){
        this.studentID = studentID;
        this.studentTotalPoints = studentTotalPoints;
        this.maxTotalPoints = maxTotalPoints;
        double ratio = studentTotalPoints/maxTotalPoints;
        if(ratio >= 0.9 && ratio <=1.0){
            grade = "A";
        }
        else if(ratio >= 0.8 && ratio < 0.9){
            grade = "B";
        }
        else if(ratio >= 0.7 && ratio < 0.8){
            grade = "C";
        }
        else if(ratio >= 0.6 && ratio < 0.7){
            grade = "D";
        }else{
            grade = "F";
        }
    }
    //------------------------------------------------
    public String displayLink(){
        if(studentID < 100)
            return "Student *00" + studentID +
                ":\n   total points: " + studentTotalPoints + " out of " + maxTotalPoints +
                "\n   grade: " + grade;
        else if(studentID > 99 && studentID < 1000)
            return "Student *0" + studentID +
                    ":\n   total points: " + studentTotalPoints + " out of " + maxTotalPoints +
                    "\n   grade: " + grade;
        else
            return "Student *" + studentID +
                    ":\n   total points: " + studentTotalPoints + " out of " + maxTotalPoints +
                    "\n   grade: " + grade;
    }
}
////////////////////////
class LinkList{
    private Link first;
    private static int numberOfStudents;
    //------------------------------------------------
    public LinkList(){
        first = null;
        numberOfStudents = 0;
    }
    //------------------------------------------------
    public boolean isEmpty() {
        return (first==null);
    }
    //------------------------------------------------
    public void insertFirst(int studentID, double studentTotalPoints, double maxTotalPoints)
    {
        Link newLink = new Link(studentID, studentTotalPoints, maxTotalPoints);
        if(isEmpty()){
            first = newLink;
            numberOfStudents++;
        }
        else {
            newLink.next = first;
            first = newLink;
            numberOfStudents++;
        }
    }
    //------------------------------------------------
    public void displayList(String path) throws IOException {
        String p = path.substring(0, path.lastIndexOf('/')+1) + "StudentGrades.txt";
        File file = new File (p);
        PrintWriter sr = new PrintWriter (file);
        sr.println ("Students' grades:");
        sr.println ("===================================================");

        Link current = first;
        while(current != null)
        {
            sr.println(current.displayLink());
            sr.println("--------------------------------------------");
            current = current.next;
        }
        sr.close ();
    }
    //------------------------------------------------
    public String[] returnArrayString(){
        String[] studentStringList = new String[numberOfStudents];
        int i = numberOfStudents-1;
        Link current = first;

        while(current != null)
        {
            String full = "";
            if(current.studentID < 100) {
                full = "00" + Integer.toString(current.studentID);
                studentStringList[i--] = full;
            }
            else if(current.studentID > 99 && current.studentID < 1000) {
                full = "0" + Integer.toString(current.studentID);
                studentStringList[i--] = full;
            }
            else
                studentStringList[i--] = Integer.toString(current.studentID);
            current = current.next;
        }
        return studentStringList;
    }
    //------------------------------------------------
    public String returnStudentData(int key){
        return find(key).displayLink();
    }
    //------------------------------------------------
    public Link find(int key)
    {
        Link current = first;
        while(current.studentID!= key)
        {
            if(current.next == null)
                return null;
            else
                current = current.next;
        }
        return current;
    }
}
////////////////////////
class JFileChoose{
    LinkList students;
    String pathToPrint;
    public JFileChoose(){
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFileChooser chooser = new JFileChooser();
        File fl = new File ("C:/");
        File NamePath;
        int checker;
        chooser.setCurrentDirectory(fl);
        chooser.setDialogTitle("Choose a file");
        checker = chooser.showOpenDialog(null);

        if(checker == JFileChooser.APPROVE_OPTION){
            NamePath = chooser.getSelectedFile();
            pathToPrint = NamePath.getAbsolutePath();
            readFile(pathToPrint);
        } else {
            JOptionPane.showMessageDialog(null, "You have clicked Cancel :( ",
                    "Cancel dialog box", JOptionPane.WARNING_MESSAGE);
        }
    }
    //------------------------------------------------
    public void readFile(String path){
        students = new LinkList();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String aLineFromFile = br.readLine();
            String[] array = aLineFromFile.split(",");
            double total = Double.parseDouble(array[array.length - 1]);
            br.readLine();

            while ((aLineFromFile = br.readLine()) != null) {
                String[] arr = aLineFromFile.split(",");
                if (arr[0].equals("Average"))
                    continue;
                else {
                    int id = Integer.parseInt(arr[0].substring(arr[0].indexOf('-') + 1));
                    students.insertFirst(id, Double.parseDouble(arr[arr.length - 1]), total);
                }
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------------
    public void chooseStudent(){
        String[] choices = students.returnArrayString();
        JDialog.setDefaultLookAndFeelDecorated(true);
        String input = (String) JOptionPane.showInputDialog(null, "Choose now...",
                "The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null,
                choices,
                choices[0]);
        JDialog.setDefaultLookAndFeelDecorated(true);
        int in = Integer.parseInt(input);
        JOptionPane.showMessageDialog(null, students.returnStudentData(in));
    }
    //------------------------------------------------
    public void printGradesToFile() throws IOException{
        if (JOptionPane.showConfirmDialog(null,
                "Do you want to print students grades to .txt file?",
                "Pint or not to print, that is the question",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            students.displayList(pathToPrint);
            String path = pathToPrint.substring(0, pathToPrint.lastIndexOf('/')+1) + "StudentGrades.txt";
            JOptionPane.showMessageDialog(null,
                    "Created file: " + path + "\nHave a great day! :) ",
                    "File is successfully printed!", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Have a great day! :( ",
                    "You have pressed \"NO\" button!", JOptionPane.WARNING_MESSAGE);
        }
    }
    //------------------------------------------------
    public boolean again(){
        if (JOptionPane.showConfirmDialog(null, "Wanna check next student?", "Again?",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }
}
//////
public class GradeCalculator {
    public static void main(String[] a) throws IOException{
        JDialog.setDefaultLookAndFeelDecorated(true);
        JOptionPane.showMessageDialog(null, "This program will load a file with students grades."+
                "\nYou'll choose student's last 4 digits number and program will show the current grade.");

        JDialog.setDefaultLookAndFeelDecorated(true);
        JOptionPane.showMessageDialog(null,"Choose a .txt or .csv file with students grades!",
                "important", JOptionPane.WARNING_MESSAGE);

        JFileChoose fc = new JFileChoose();

        JDialog.setDefaultLookAndFeelDecorated(true);
        do {
            fc.chooseStudent();
        } while(fc.again());

        fc.printGradesToFile();
    }
}
