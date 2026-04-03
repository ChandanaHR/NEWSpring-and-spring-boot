//Model class
  public class User {
    private Date dob;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
//HTML Form
<form action="/save" method="post">
    DOB: <input type="text" name="dob" placeholder="yyyy-MM-dd">
    <button type="submit">Submit</button>
</form>

  //Custom Property Editor
  import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(text);
            setValue(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//Register editor in Controller
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
public class UserController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }
     @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {

        System.out.println(user.getDob()); // Date object

        return "success"; // JSP/HTML page
    }
  
}
