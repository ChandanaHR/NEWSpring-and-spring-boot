1a) SecurityContextHolderFilter
  Normal java code
  public class LoginDemo {
    public static void main(String[] args) {
        // Step 1: Check the credentials
        String username = "chandana";
        String password = "pass123";
        if (username.equals("chandana")
                && password.equals("pass123")) {
            // Step 2: Remember who logged in
            String loggedInUser = username;
            // Step 3: Use the identity later
            System.out.println(
                "Welcome " + loggedInUser
            );
        }
    }
}
Notice that the program checks the password only in Step 1.
After that, it uses loggedInUser to know who has logged in. It does not need to compare the password again just to display the welcome message.
In a real application, you wouldn't manage authentication this way. This is only to explain the concept.
  First, imagine this situation
You log in to a website.
- Username: chandana
- Password: 1234
The website checks your username and password. They are correct, so you are logged in.
Now you click My Profile.
The website needs to know: Who is viewing this profile?
It already knows that you logged in as chandana. It should not ask you to enter your password again just to display your name.
This is where SecurityContextHolder becomes useful. It lets your application access the identity of the currently authenticated user.
