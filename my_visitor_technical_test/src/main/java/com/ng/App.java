package com.ng;

import com.ng.business.Item;
import com.ng.business.furnitures.Chair;
import com.ng.business.furnitures.Table;
import com.ng.business.structures.Door;
import com.ng.business.structures.Room;
import com.ng.business.structures.Window;
import com.ng.businessvisitor.CountVisitor;
import com.ng.businessvisitor.PrintVisitor;

/**
 * 
 * Nicolas Guignard - Custom Test Visitor
 * Java programming test
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Item chair0 = new Chair();
        Item chair1 = new Chair();
        Item chair2 = new Chair();
        Item chair3 = new Chair();
        
        Item[] chairs = {chair2, chair3};
        Item table = new Table();
        
        Item window = new Window();
        Item door = new Door();
        Item room = new Room(chairs);
        
        CountVisitor countVisitor = new CountVisitor();
        PrintVisitor printVisitor = new PrintVisitor();

        chair0.accept(countVisitor);
        chair0.accept(printVisitor);
        
        chair1.accept(countVisitor);
        chair1.accept(printVisitor);
        
        System.out.println("------------------------");
        System.out.println("number of chairs visited : " + countVisitor.getCounter());
        System.out.println("------------------------");
        
        table.accept(countVisitor);
        table.accept(printVisitor);
        
        System.out.println("------------------------");
        
        window.accept(countVisitor);
        window.accept(printVisitor);
        
        System.out.println("------------------------");
        
        room.accept(countVisitor);
        room.accept(printVisitor);
        
        System.out.println("------------------------");
        
        door.accept(countVisitor);
        door.accept(printVisitor);
        
        System.out.println("------------------------");
        System.out.println("total of visited items : " + countVisitor.getCounter());
    }
}
