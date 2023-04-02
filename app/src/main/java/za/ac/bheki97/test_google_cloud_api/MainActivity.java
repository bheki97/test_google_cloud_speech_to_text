package za.ac.bheki97.test_google_cloud_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.SpeechSettings;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GoogleCredentials credentials = null;
        SpeechSettings speechSettings = null;
        SpeechClient speechClient = null;
        byte[] data=null;
        Path path= null;
        try {
            credentials = GoogleCredentials.fromStream(getResources().openRawResource(R.raw.google_credentials));
            speechSettings = SpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();
            speechClient = SpeechClient.create(speechSettings);

            path = Paths.get("path/to/your/audio/file");
            data = Files.readAllBytes(path);

        } catch (IOException e) {
            e.printStackTrace();
        }



        RecognitionAudio recognitionAudio =
                RecognitionAudio.newBuilder()
                        .setContent(ByteString.copyFrom(data))
                        .build();

        RecognitionConfig recognitionConfig =
                RecognitionConfig.newBuilder()
                        .setLanguageCode("en-US")
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setSampleRateHertz(8000)
                        .build();


        SpeechRecognitionResult result = speechClient.recognize(recognitionConfig, recognitionAudio).getResultsList().get(0);
        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
        String transcribedText = alternative.getTranscript();
    }
}