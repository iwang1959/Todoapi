import Controller.ContactsController;
import Model.Contact;
import io.javalin.Javalin;

import java.util.ArrayList;

public class main {


    public static void main(String[] args) {
        //initContacts();
        Javalin app = Javalin.create().start(8981);
        app.get("/", ctx ->{
            ctx.result("I'm hearing you");
        });

        ContactsController ctrlContacts = new ContactsController(app);



        }
    }
