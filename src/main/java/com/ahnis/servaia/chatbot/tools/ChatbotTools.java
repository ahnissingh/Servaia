package com.ahnis.servaia.chatbot.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class ChatbotTools {

    @Tool(description = "Provide suicide prevention resources when the user expresses suicidal behavior.")
    public String provideSuicidePreventionResources(@ToolParam(description = "Country to provide prevention contacts of the user. if missing ask user for country name.") String country) {
        return """
                If you or someone you know is struggling, please reach out to these resources:
                - National Suicide Prevention Lifeline (USA): 1-800-273-TALK (1-800-273-8255)
                - Crisis Text Line (USA): Text HOME to 741741
                - Samaritans (UK): 116 123
                - Lifeline (Australia): 13 11 14
                - International Suicide Hotlines: https://www.suicidestop.com/call_a_hotline.html
                You are not alone. Help is available.
                """;
    }

}
