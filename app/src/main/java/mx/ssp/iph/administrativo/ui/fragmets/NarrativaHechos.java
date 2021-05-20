package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.iph.administrativo.viewModel.NarrativaHechosViewModel;
import mx.ssp.iph.R;

import static android.app.Activity.RESULT_OK;

public class NarrativaHechos extends Fragment {

    private NarrativaHechosViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtNarrativaHechos;
    private ImageView imgMicrofonoNarrativaHechos;

    public static NarrativaHechos newInstance() {
        return new NarrativaHechos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.narrativa_hechos_fragment, container, false);

        txtNarrativaHechos = (TextView)view.findViewById(R.id.txtNarrativaHechos);
        imgMicrofonoNarrativaHechos = (ImageView) view.findViewById(R.id.imgMicrofonoNarrativaHechos);

        imgMicrofonoNarrativaHechos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
                Toast.makeText(getContext(),"Hola",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void iniciarEntradadeVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"COMIENZA A HABLAR AHORA");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NarrativaHechosViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if (resultCode== RESULT_OK && null != data)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String textoActual = txtNarrativaHechos.getText().toString();
                    txtNarrativaHechos.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }
}