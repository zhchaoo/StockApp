package jxt.app.microblog.manage;

interface IMicroblogManager {
    String AddContacts(String u_id);
    
    boolean DelContacts(String u_id);
    
    String GetMicroblogById(String u_id);
    
    String GetMicroblog();
    
    List<String> UpdateMicroblog();
}