/*
 * Copyright (c) 2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package bulkSendToRepeater.contextmenu;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class MyContextMenuItemsProvider implements ContextMenuItemsProvider
{

    private final MontoyaApi api;

    public MyContextMenuItemsProvider(MontoyaApi api)
    {

        this.api = api;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event)
    {
        if (event.isFromTool(ToolType.PROXY, ToolType.TARGET, ToolType.LOGGER, ToolType.INTRUDER, ToolType.REPEATER))
        {
            List<Component> menuItemList = new ArrayList<>();
            //JSONArray rulesArray = new JSONArray(); //an array of JSON objects representing single rules

            List<HttpRequestResponse> requestResponses = new ArrayList<>();
            //If the context menu was opened within the Request/Response viewer
            if(event.messageEditorRequestResponse().isPresent()){
                requestResponses.add(event.messageEditorRequestResponse().get().requestResponse());
            //else if it was opened from the list in Proxy/Logger
            } else {
                requestResponses = event.selectedRequestResponses();
            }
            
            final List<HttpRequestResponse> requestResponsesFinal = requestResponses;
            
            //create JMenuItems
            JMenuItem titled = new JMenuItem("Use Method + Path as Repeater Tab Title");
            titled.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    for(HttpRequestResponse requestResponse : requestResponsesFinal) {
 
                        burp.api.montoya.http.HttpService requestService = requestResponse.httpService();
                        String method = requestResponse.request().method();
                        //first 70 characters of path
                        String path = requestResponse.request().path();
                        if(path.length() >= 70){
                            path = path.substring(0,69);
                        }
                        
                        api.repeater().sendToRepeater(requestResponse.request(), method + " " + path);
                    }

                }
            });

            menuItemList.add(titled);
            
            JMenuItem numbered = new JMenuItem("Use Numbers as Repeater Tab Title (Default)");
            numbered.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    //api.logging().logToOutput("In ALL execution. Rules array:" + rulesArray.toString());
                    for(HttpRequestResponse requestResponse : requestResponsesFinal) {
                        api.repeater().sendToRepeater(requestResponse.request());
                    }
                    

                }
            });

            menuItemList.add(numbered);            

            
            return menuItemList;
        }

        return null; //not acting from scoped Burp toolType
    }
}