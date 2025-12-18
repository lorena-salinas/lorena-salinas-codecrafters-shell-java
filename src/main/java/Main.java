
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {

        while (true) {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            // Separate the command name from the rest of the line
            String split[] = input.split(" ", 2);
            String command = split[0];

            if (command.equals("exit")) {
                break;
            }
            if (command.equals("echo")){
                String message = split[1];
                System.out.println(message);
                continue;
            }
            if (command.equals("pwd")){
                System.out.println(Paths.get("").toAbsolutePath().toString());
                continue;
            }
            if (command.equals("cd")){
                String newPath; // stores the directory user wants to change to
                // User typed only cd with no path set directory to user's HOME directory
                if (split.length < 2) {
                    newPath = System.getenv("HOME");
                    System.setProperty("user.dir", newPath);
                }
                // Otherwise, validate that a path was provided, that the path exists, and that it is a directory and not a file
                else if (split.length == 2 && Files.exists(Paths.get(split[1])) && Files.isDirectory(Paths.get(split[1]))) {
                    newPath = Paths.get(split[1]).toAbsolutePath().toString(); // converts the given path into an absolute path
                    System.setProperty("user.dir", newPath); //change the shell's current directory to the new one
                }
                else {
                    System.out.println("cd: " + split[1] + " : No such file or directory");
                }
                continue;
            }
            if (command.equals("type")){
                String command_or_file_name = split[1].trim();

                // Check for shell builtins
                if (command_or_file_name.equals("exit") || command_or_file_name.equals("echo") || command_or_file_name.equals("type") || command_or_file_name.equals("pwd")) {
                    System.out.println(command_or_file_name + " is a shell builtin");
                }
                else {
                    String foundFilePath = findExecutable(command_or_file_name);
                    boolean found = foundFilePath != null;
                    if (found == true) {
                        System.out.println(command_or_file_name + " is " + foundFilePath);
                    } else {
                        System.out.println(command_or_file_name + ": not found");
                    }
                }
                continue;
            }

            // For external programs
            String[] command_args = input.split(" "); // each argument as a separate string

            String program = command_args[0];
            String prog_path = findExecutable(program);
            if (prog_path == null) {
                System.out.println(program + ": command not found");
                continue;

            }

            ProcessBuilder builder = new ProcessBuilder(command_args);
            builder.inheritIO(); // connects programs output to shell
            Process process = builder.start();
            process.waitFor();

            }
        }

    // Helper to locate executables in PATH
    private static String findExecutable(String command) {
        // Get the PATH environment variable
        String path = System.getenv("PATH");

        if (path != null) {
            // Split PATH into individual directories
            String[] directories = path.split(":");
            //Iterate over each
            for (String directory : directories) {
                File filePath = new File(directory, command); // construct full path directory/command_or_file_name
                // Check if it exists in the current directory and if it is executable
                if (filePath.exists() && Files.isExecutable(filePath.toPath())) {
                    return filePath.getAbsolutePath();
                }
            }
        }
        return null;
    }
}
