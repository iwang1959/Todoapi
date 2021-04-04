package Controller;

import Model.Contact;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;

public class ContactsController {

    private Javalin app = null;
    private static ArrayList<Contact>  contacts = Contact.getContacts();

    public ContactsController(Javalin app) {
        this.app = app;
        app.get("/contacts", ContactsController::get);
        app.get("/contacts/:id", ContactsController::getOne);
        app.post("/contacts/:name/:phone", ContactsController::post);
        app.put("/contacts/:id/:name/:phone", ContactsController::put);
        app.patch("/contacts/:id/:name/:phone", ContactsController::patch);
        app.delete("/contacts/:id/", ContactsController::delete);

    }

    public static void get(Context ctx){
        ctx.json(contacts);
    }

    public static void getOne(Context ctx){
        int idx = -1;

        try{
            idx = Integer.parseInt(ctx.pathParam("id"));
            if(idx > -1 && idx < contacts.size()){
                ctx.json(contacts.get(idx));
            }else{
                ctx.result("Index is out of boundaries").status(404);
            }

        }catch(Exception ex) {
            ctx.result("Unknown entry when creating phone entry").status(404);
        }


    }

    public static void post(Context ctx){
        String name = null;
        String phone = null;

        try{

            name = ctx.pathParam("name");
            phone = ctx.pathParam("phone");

            if(name.trim().length() > 0 && phone.trim().length() > 0){
                boolean unique = true;
                for(int i = 0; i<contacts.size(); ++i){
                    Contact con = contacts.get(i);
                    if(name.equals(con.getName())){
                        unique = false;
                        break;
                    }
                }
                if(unique) {
                    contacts.add(new Contact(name, phone));
                    Contact.saveContacts();
                    ctx.result("Added").status(200);
                }else {
                    ctx.result("Please use a unique name").status(404);
                }

            } else{
                ctx.result("You need to have a value for either the name or the phone number").status(404);

            }

        }catch(Exception ex) {
            ctx.result("Unknown entry when creating phone entry").status(404);
        }

    }

    public static void put(Context ctx){
        try {
            String name = ctx.pathParam("name");
            String phone = ctx.pathParam("phone");
            int idx = Integer.parseInt(ctx.pathParam("id"));

            if(idx > -1 && idx < contacts.size()){
                Contact c  = new Contact(name, phone);
                contacts.add(idx, c);
                contacts.remove(idx +1);
                Contact.saveContacts();
                ctx.result("Replaced");


            }else{
                ctx.result("Index of contacts is out of range").status(404);

            }

        }catch(Exception ex){
            ctx.result("Unknown entry when replacing phone entry").status(404);
        }

    }

    public static void patch(Context ctx){
        try {
            String name = ctx.pathParam("name");
            String phone = ctx.pathParam("phone");
            int idx = Integer.parseInt(ctx.pathParam("id"));

            Contact c = contacts.get(idx);

            if(name.trim().length() > 0)
                c.setName(name);

            if(phone.trim().length() > 0)
                c.setPhone(phone);

            Contact.saveContacts();
            ctx.result("Modified");

        }catch(Exception ex){
            ctx.result("Unknown error occurred while trying to perform modification to entry").status(404);
        }

    }

    public static void delete(Context ctx){
        try{
            int idx = Integer.parseInt(ctx.pathParam("id"));

            if(idx < 0 || idx >= contacts.size()) {
                ctx.result("out of expected boundaries of contacts list").status(404);
            }else{
                contacts.remove(idx);
                Contact.saveContacts();
                ctx.result("Deleted entry id: " + idx);
            }


        }catch(Exception ex) {
            ctx.result("Unknown error when deleting entry").status(404);
        }

    }
}
