package me.ilucah.audiovisualiser_service.controller;

import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.ChatModel;
import com.openai.models.ReasoningEffort;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import me.ilucah.audiovisualiser_service.model.PromptResolve;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;

@RestController
public class PromptController {

    @Value("${openai.api-key}")
    private String apiKey;

    private OpenAIClientAsync client;

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClientAsync.builder()
                .apiKey(apiKey)
                .build();
    }

    @PostMapping("/prompt")
    public String prompt(@RequestBody String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_6_ASTRA)
                .addSystemMessage("""
                                You generate OpenGL GLSL 330 core shaders.
                                Output ONLY the fragment shader code.
                                No explanations.
                                No markdown.
                                No extra text.
                                The uniforms you have access to are:
                                uniform int time;
                                uniform float leftRMS;
                                uniform float rightRMS;
                                uniform float screenWidth;
                                uniform float screenHeight;
                                uniform float audioBufferTD[256];
                                uniform float audioBufferFD[128];
                                The uniform audioBufferTD is a buffer of data in the time domain.
                                The uniform audioBufferFD is a buffer of data in the frequency domain.
                                
                                When using polar coordinates derived from atan(y, x), the entire visual representation must be periodic across the -PI/+PI wrap boundary, not only array sampling.
                                
                                Any value derived directly from the angular coordinate must satisfy:
                                f(-PI) == f(+PI)
                                
                                Do not use a linearly mapped angular coordinate in non-periodic functions such as smoothstep(), mix(), gradients, thresholds, masks, or colour interpolation unless the result is explicitly made periodic.
                                
                                Prefer sin(angle), cos(angle), or periodic/circular interpolation for angular effects.
                                
                                When converting angle to [0,1], remember that 0 and 1 represent the same angular position and must produce identical visual values.
                                """)
                .addUserMessage(prompt)
                .reasoningEffort(ReasoningEffort.MEDIUM)
                .build();
        try {
            ChatCompletion completion = client.chat()
                    .completions()
                    .create(params)
                    .get();

            String response = completion.choices()
                    .get(0)
                    .message()
                    .content().get();

            return PromptResolve.builder()
                    .success(true)
                    .prompt(response)
                    .build()
                    .toJson();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("OpenAI request failed", e);
        }
    }
}