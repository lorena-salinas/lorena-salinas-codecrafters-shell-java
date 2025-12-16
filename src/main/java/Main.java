
import java.nio.file.Files;
import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {

        while (true) {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            String split[] = command.split(" ", 2);
            command = split[0];

            if (command.equals("exit")) {
                break;
            }
            if (command.equals("echo")){
                String message = split[1];
                System.out.println(message);
                continue;
            }
            if (command.equals("type")){
                String command_or_file_name = split[1];

                // Check for shell builtins
                if (command_or_file_name.equals("exit") || command_or_file_name.equals("echo") || command_or_file_name.equals("type")) {
                    System.out.println(command_or_file_name + " is a shell builtin");
                }
                else {
                    // Get the PATH environment variable
                    String path = System.getenv("PATH");
                    boolean found = false;

                    if (path != null) {
                        // Split PATH into individual directories
                        String[] directories = path.split(":");
                        //Iterate over each
                        for (String directory : directories) {
                            File filePath = new File(directory, command_or_file_name); // construct full path directory/command_or_file_name
                            // Check if it exists in the current directory and if it is executable
                            if (filePath.exists() && Files.isExecutable(filePath.toPath())) {
                                System.out.println(command_or_file_name + " is " + filePath.getAbsolutePath());
                                found = true;
                                break;
                            }
                        }
                    } if (!found) {
                        System.out.println(command_or_file_name + ": not found");
                    }
                }
                continue;
            }
            System.out.println(command + ": command not found");
        }
    }
}
