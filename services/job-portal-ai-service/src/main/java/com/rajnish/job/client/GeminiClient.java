
package com.rajnish.job.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.rajnish.job.config.GeminiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final Client genAiClient;
    private final GeminiProperties geminiProperties;
    private final ObjectMapper objectMapper;

    public String generateText(String systemInstruction,String prompt){
        return generateText(systemInstruction,
                prompt,
                geminiProperties.getTemperature(),
                geminiProperties.getMaxOutputTokens());
    }

    public <T> T generateJson(String systemInstruction,String prompt, Class<T> responseType){
        return callJson(systemInstruction,prompt,responseType);
    }

    public String generateText(String systemInstruction,String prompt, double temperature, int maxTokens) {
        return callText(systemInstruction, prompt,
                (float) geminiProperties.getTemperature(),
                geminiProperties.getMaxOutputTokens());
    }

    private <T> T callJson(String systemInstruction, String prompt, Class<T> responseType) {
        try{
            GenerateContentConfig config = buildConfig(systemInstruction, 0.3f,
                    geminiProperties.getMaxOutputTokens(),true);
            GenerateContentResponse response = genAiClient.models
                    .generateContent(geminiProperties.getModel(), prompt, config);

            String text = response.text();
            return objectMapper.readValue(response.text(),responseType);
        }

        catch (Exception e) {
            throw new RuntimeException("Failed to get response from Gemini", e);
        }
    }


    private String callText(String systemInstruction, String prompt, float temperature, int maxTokens) {
        try{
            GenerateContentConfig config = buildConfig(systemInstruction, temperature, maxTokens,false);
            GenerateContentResponse response = genAiClient.models
                    .generateContent(geminiProperties.getModel(), prompt, config);

            String text = response.text();
            return text;
        }

        catch (Exception e) {
            throw new RuntimeException("Failed to get response from Gemini", e);
        }
    }

    private GenerateContentConfig buildConfig(String systemInstruction, float temperature,
                                              int maxTokens, boolean jsonMode) {

        GenerateContentConfig.Builder builder = GenerateContentConfig.builder()
                .temperature((float) temperature).maxOutputTokens(maxTokens);

        if(systemInstruction != null && !systemInstruction.isBlank()){
            builder.systemInstruction(
                    Content.fromParts(Part.fromText(systemInstruction))
            );

        }
        if(jsonMode){
            builder.responseMimeType("application/json");
        }
        return builder.build();
    }
}
