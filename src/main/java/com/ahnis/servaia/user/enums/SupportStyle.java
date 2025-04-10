package com.ahnis.servaia.user.enums;


import com.ahnis.servaia.user.util.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
//Temperature can be used by open ai api to set the model temperature (Can use it when required so used @Getter
public enum SupportStyle {
    FRIENDLY("Use warm, approachable language. Focus on building trust and rapport.", 0.6),
    REALISTIC("Provide practical, evidence-based advice. Avoid sugarcoating.", 0.4),
    MOTIVATIONAL("Offer encouraging, uplifting responses. Highlight strengths and progress.", 0.7),
    MINDFUL("Emphasize present-moment awareness. Use grounding techniques.", 0.5),
    ANALYTICAL("Break down problems logically. Offer structured approaches.", 0.3),
    COMPASSIONATE("Show deep empathy and understanding. Validate feelings.", 0.6),
    HUMOROUS("Use appropriate humor to lighten mood. Maintain professionalism.", 0.8),
    EXISTENTIAL("Explore deeper meaning and purpose. Ask reflective questions.", 0.5);

    private final String description;
    private final double temperature;

    SupportStyle(String description, double temperature) {
        this.description = description;
        this.temperature = temperature;
    }

    @JsonCreator
    public static SupportStyle from(String value) {
        return EnumUtils.fromString(SupportStyle.class, value);
    }
}
