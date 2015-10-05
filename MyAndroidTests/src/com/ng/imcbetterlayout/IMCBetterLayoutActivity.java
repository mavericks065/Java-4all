package com.ng.imcbetterlayout;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ng.R;

public class IMCBetterLayoutActivity extends Activity {

	private final String defaut = "Vous devez cliquer sur le bouton « Calculer l'IMC » pour obtenir un résultat.";

	Button envoyer = null;
	Button raz = null;

	EditText poids = null;
	EditText taille = null;

	RadioGroup group = null;

	TextView result = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imcbetter_layout);

		envoyer = (Button) findViewById(R.id.calcul);

		raz = (Button) findViewById(R.id.raz);

		taille = (EditText) findViewById(R.id.taille);
		poids = (EditText) findViewById(R.id.poids);

		group = (RadioGroup) findViewById(R.id.group);

		result = (TextView) findViewById(R.id.result);

		envoyer.setOnClickListener(envoyerListener);
		raz.setOnClickListener(razListener);
		taille.addTextChangedListener(textWatcher);
		poids.addTextChangedListener(textWatcher);

	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			result.setText(defaut);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private OnClickListener envoyerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String t = taille.getText().toString();
			String p = poids.getText().toString();

			float tValue = Float.valueOf(t);

			if (tValue == 0)
				Toast.makeText(
						IMCBetterLayoutActivity.this,
						"Vous ne pouvez calculer un IMC avec une taille égale à 0.",
						Toast.LENGTH_SHORT).show();
			else {
				float pValue = Float.valueOf(p);
				if (group.getCheckedRadioButtonId() == R.id.radio2)
					tValue = tValue / 100;

				tValue = (float) Math.pow(tValue, 2);
				float imc = pValue / tValue;
				result.setText("Votre IMC est " + String.valueOf(imc));
			}
		}
	};

	private OnClickListener razListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			poids.getText().clear();
			taille.getText().clear();
			result.setText(defaut);
		}

	};

}
