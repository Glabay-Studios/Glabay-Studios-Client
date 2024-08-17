package com.client.ui;

import java.text.DateFormat;
import java.util.Date;

import com.client.Client;
import com.client.Configuration;
import com.client.Rasterizer2D;

public class DevConsole {

    public static String currentTime() {
        DateFormat date = DateFormat.getTimeInstance();
        return date.format(new Date());
    }

    public String time_stamp()
    {
        return currentTime().replaceAll("AM", "").replaceAll("PM", "");
    }

    public void open_console(int key)
    {
        if(key == 96)
            console_open = !console_open;
    }

    public void print_message(String message, int id)
    {
        if(client().backDialogID == -1)
            client().inputTaken = true;

        for(int line = 499; line > 0; line--)
            console_print[line] = console_print[line - 1];

        message_id++;
        console_print[0] = time_stamp() + ": " + (id == 0 ? "--> " : "") + message;
    }

    public void command_input(int j)
    {
        if(j == 8 && console_input.length() > 0 && console_input.length() <= 40)
            console_input = console_input.substring(0, console_input.length() - 1);

        if(j >= 32 && j <= 122 && console_input.length() < 40)
            console_input += (char)j;

        if((j == 13 || j == 10) && console_input.length() > 0 && console_input.length() <= 40)
        {
            print_message(console_input, 0);
            send_command_packet(console_input);
            console_input = "";
            client().inputTaken = true;
        }
    }

    public void send_command_packet(String command)
    {
        if(command.equalsIgnoreCase("cls"))
            for(int line = 0; line < 500; line++)
                console_print[line] = null;

        handleCommands();
//        client().data.createFrame(103);
//        client().data.writeWordBigEndian(command.length() + 1);
//        client().data.writeString(command);
    }


    public void draw_console()
    {
        if(console_open)
        {
            Rasterizer2D.drawPixelsWithOpacity2(4, 4, 512, 334, 5320850, 80);
            Rasterizer2D.drawPixels(1, 315, 4, 0xFFFFFF, 512);
            client().newBoldFont.drawBasicString("--> " + console_input + (client().loopCycle % 20 < 10 ? "|" : ""), 11, 330, 0xFFFFFF, 0);// client().newSmallFont.drawToLeft("Build: " + 317, 505, 0xFFFFFF, 330, 256, 1);//505, 312 above the divider
            client().newSmallFont.drawString(Configuration.CLIENT_TITLE, 4, 12, 0xFFFFFF, 0, 256);
        }
        draw_console_messages();
    }

    public void draw_console_messages()
    {
        if(console_open)
        {
            if(message_id == 1)
            {
                print_message(default_message, 1);
                return;
            }
            int output_y = -3;
            int y_pos = 0;
            scroller_offset = 0;
            scroller_pos = client().scrollbar_position;
            Rasterizer2D.setDrawingArea(315, 0, 510, 21);
            for(int line = 0; line < 500; line++)
            {
                y_pos = (257 - output_y * 16) + scroller_pos;
                if(console_print[line] != null)
                {
                    scroller_index = line - 1;
                    scroller = (scroller_index - 1 > 14 ? true : false);
                    client().newRegularFont.drawBasicString(console_print[line], 9, y_pos, 0xFFFFFF, 0);
                    scroller_offset++;
                    output_y++;
                }
            }
            if(scroller)
                client().draw_scrollbar(494, 22, 0, 270, scroller_offset, 18, 17, 0);

            Client.rasterProvider.setRaster();
        }
    }

    public static Client client()
    {
        return Client.instance;
    }

    public DevConsole()
    {
        message_id = 1;
        console_input = "";
        console_print = new String[500];
        console_open = false;
        default_message = "This is the developer console. To close, press the ` key on your keyboard.";
        scroller = false;
    }

    public int message_id;
    public String console_input;
    public final String[] console_print;
    public boolean console_open;
    public final String default_message;
    public boolean scroller;
    public int scroller_index;
    public int scroller_pos;
    public int scroller_offset;

    public void handleCommands() {

    }
}
