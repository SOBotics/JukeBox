package in.bhargavrao.stackoverflow.jukebox;

import org.sobotics.chatexchange.chat.ChatHost;
import org.sobotics.chatexchange.chat.Room;
import org.sobotics.chatexchange.chat.StackExchangeClient;
import org.sobotics.chatexchange.chat.event.EventType;
import org.sobotics.chatexchange.chat.event.UserMentionedEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class RunJukey {
   public static List<String[]> songList;

   public RunJukey() {
   }

   public static void main(String[] args) {
      Properties prop = new Properties();

      try {
         prop.load(new FileInputStream("./properties/login.properties"));
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      String email = prop.getProperty("email");
      String password = prop.getProperty("password");
      StackExchangeClient client = new StackExchangeClient(email, password);
      Room sobotics = client.joinRoom(ChatHost.STACK_OVERFLOW, 111347);
      sobotics.send("[Juker](https://git.io/v1lO9) started");
      songList = getCSVFromList(getSongs());
      sobotics.addEventListener(EventType.USER_MENTIONED, (event) -> {
         mention(sobotics, event, true);
      });
   }

   private static void mention(Room room, UserMentionedEvent event, boolean b) {
      String message = event.getMessage().getContent();
      System.out.println(message);
      String[] song = getARandomSong();
      if (message.toLowerCase().contains("alive")) {
         room.send("[juker] Yes.");
      }

      if (message.toLowerCase().contains("commands juker")) {
         room.send("There are no commands as such, just use `song` somewhere in your message and I'll fetch you one");
      }

      if (message.toLowerCase().split(" ")[1].equals("commands") && message.split(" ").length == 2) {
         room.send("[juker] Try `commands juker`");
      }

      if (message.toLowerCase().contains("song")) {
         room.send("[ [Juker](https://git.io/v1lO9) ] [" + song[0] + "](" + song[1] + ")");
      }

      if (message.toLowerCase().split(" ")[1].equals("refetch")) {
         songList = getCSVFromList(getSongs());
      }

   }

   private static String[] getARandomSong() {
      int number = songList.size();
      int random = (int)(Math.random() * (double)number);
      return (String[])songList.get(random);
   }

   private static String[] getARandomSongFromGenre(String requiredGenre) {
      List<String[]> genreSongList = new ArrayList();
      Iterator var2 = songList.iterator();

      while(var2.hasNext()) {
         String[] songs = (String[])var2.next();
         String[] songGenre = songs[2].split(";");
         if (Arrays.asList(songGenre).contains(requiredGenre)) {
            genreSongList.add(songs);
         }
      }

      int number = genreSongList.size();
      int random = (int)(Math.random() * (double)number + 1.0D);
      return (String[])songList.get(random);
   }

   private static List<String[]> getCSVFromList(List<String> list) {
      List<String[]> csv = new ArrayList();
      Iterator var2 = list.iterator();

      while(var2.hasNext()) {
         String i = (String)var2.next();
         csv.add(i.split(","));
      }

      return csv;
   }

   private static List<String> getSongs() {
      String songList = "https://raw.githubusercontent.com/SOBotics/JukeBox/master/songlist.csv";
      ArrayList songs = new ArrayList();

      try {
         URL stockUrl = new URL(songList);
         BufferedReader in = new BufferedReader(new InputStreamReader(stockUrl.openStream()));
         String text = null;
         text = in.readLine();

         while((text = in.readLine()) != null) {
            songs.add(text);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return songs;
   }
}

