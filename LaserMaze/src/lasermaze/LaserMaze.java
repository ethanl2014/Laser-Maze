//Author: Ethan Lee
//Takes text file input that describes a laser in a room and outputs the distance the laser travels
//(See README)

package lasermaze;
import java.io.*;
import java.util.Scanner;

class Tile{ //symbol: -
    public char effect(char direction){
        return direction;
    }
}
class NFprism extends Tile{//symbol: ^
    @Override
    public char effect(char direction){
        return 'n';
    }
}
class EFprism extends Tile{//symbol: >
    @Override
    public char effect(char direction){
        return 'e';
    }
}
class SFprism extends Tile{//symbol: v
    @Override
    public char effect(char direction){
        return 's';
    }
}
class WFprism extends Tile{//symbol: <
    @Override
    public char effect(char direction){
        return 'w';
    }
}
class Omirror extends Tile{//symbol: O
    @Override
    public char effect(char direction){
        if(direction == 'n'){
            return 's';
        }
        if(direction == 'e'){
            return 'w';
        }
        if(direction == 's'){
            return 'n';
        } 
        return 'e';
    }
}
class NEmirror extends Tile{//symbol: \
    @Override
    public char effect(char direction){
        if(direction == 'n'){
            return 'w';
        }
        if(direction == 'e'){
            return 's';
        }
        if(direction == 's'){
            return 'e';
        } 
        return 'n';
    }
}
class SEmirror extends Tile{//symbol: /
    @Override
    public char effect(char direction){
        if(direction == 'n'){
            return 'e';
        }
        if(direction == 'e'){
            return 'n';
        }
        if(direction == 's'){
            return 'w';
        } 
        return 's';
    }
}
class Edge extends Tile{//symbol: e (used to define boundary of room)
    @Override
    public char effect(char direction){
        return 'x';
    }
}

public class LaserMaze {
    
    private static String readFile(String pathname) throws IOException {
        //Converts text file to string   
        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());        

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(System.lineSeparator());
            }
            return fileContents.toString();
        }
}
    
    public static void main(String[] args){
        try{
            //takes input and converts to string array where each line is an element
            Scanner input = new Scanner(System.in);
            String myString = input.next();
            String fileinput = readFile(myString);
            String[] edgeless_room = fileinput.split(System.getProperty("line.separator"));
            
            //one of each tile type is instantiated for later use (see line 153)
            Tile norm = new Tile();
            NFprism np = new NFprism();
            EFprism ep = new EFprism();
            SFprism sp = new SFprism();
            WFprism wp = new WFprism();
            Omirror om = new Omirror();
            NEmirror nm = new NEmirror();
            SEmirror sm = new SEmirror();
            Edge edge = new Edge();
            Tile tile_item = new Tile(); //this is used to represent the effect of the current tile (see line 153 )
            
            //core variables are established
            char direction = 'e'; //laser always starts east
            int distance = -1; //-1 due to "extra" distance from stopping on edge tile
            int x = 0; //x and y are coordinates for the room
            int y = 0; //used to track the laser moving thru tiles
            
            String edgeline = new String(new char[edgeless_room[0].length()+2]).replace("\0", "e");
            //^string of edge characters, based on room width 
            String[] room = new String[edgeless_room.length+2];
            room[0] = edgeline; //top and bottom of room are edges
            room[room.length-1] = edgeline;
            for(int i=1; i<room.length-1; i++){
                room[i] = "e" + edgeless_room[i-1] + "e"; //add edges to side of room
                if(room[i].contains("@")){ //find starting coordinates of laser
                    x = room[i].indexOf("@");
                    y = i;
                }
            }
            
            //next 3 lines are for preventing infinite loops (see line 187)
            String loop_checker = "";
            String lc_helper;    
            char old_direction;
            
            char current_tile = room[y].charAt(x); //set current tile to @
            while(direction != 'x'){ //while not at an edge:  
                switch(current_tile){//set tile effect based on char
                    case '@':
                        tile_item = norm;
                        break;  
                    case '-':
                        tile_item = norm;
                        break;  
                    case '^':
                        tile_item = np;
                        break;       
                    case '>':
                        tile_item = ep;
                        break;       
                    case '<':
                        tile_item = wp;
                        break;  
                    case 'v':
                        tile_item = sp;
                        break;  
                    case 'O':
                        tile_item = om;
                        break;  
                    case '\\':
                        tile_item = nm;
                        break;  
                    case '/':
                        tile_item = sm;
                        break;  
                    case 'e':
                        tile_item = edge;
                        break;  
                }
                old_direction = direction;
                direction = tile_item.effect(direction);
                if(direction != old_direction){
                    //catalog direction and position, infinite loop if same direction and position appears twice
                    lc_helper = direction + Integer.toString(x) + Integer.toString(y);
                    if(loop_checker.contains(lc_helper)){
                        distance = -1;
                        break;
                    }
                    loop_checker = loop_checker + lc_helper;
                }
                distance = distance + 1;
                switch(direction){//change coordinates based on direction
                    case 'n':
                        y = y-1;
                        break;  
                    case 'e':
                        x = x+1;
                        break;       
                    case 's':
                        y = y+1; 
                        break;
                    case 'w':
                        x = x-1;
                        break;  
                }
                current_tile = room[y].charAt(x); //change location based on xy
            }
            System.out.print(distance);
        }
        catch(IOException e){
        }
    }
}