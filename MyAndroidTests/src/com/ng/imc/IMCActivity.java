package com.ng.imc;

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

public class IMCActivity extends Activity {

	// Default returned String
	private final String defaultText = "Vous devez cliquer sur le bouton « Calculer l'IMC » pour obtenir un résultat.";

	private final String megaString = "Vous faites un poids parfait ! Wahou ! Trop fort ! On dirait Brad Pitt (si vous êtes un homme)/Angelina Jolie (si vous êtes une femme)/Willy (si vous êtes un orque) !";

	private Button sender = null;
	private Button reset = null;

	private EditText weight = null;
	private EditText tall = null;

	private RadioGroup group = null;

	private TextView result = null;

	private CheckBox mega = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imc);

		/**
		 * we are getting all the views we need
		 */
		sender = (Button) findViewById(R.id.calcul);
		reset = (Button) findViewById(R.id.raz);

		weight = (EditText) findViewById(R.id.poids);
		tall = (EditText) findViewById(R.id.taille);

		mega = (CheckBox) findViewById(R.id.mega);

		group = (RadioGroup) findViewById(R.id.group);

		result = (TextView) findViewById(R.id.result);

		// we give a listener for the components that need one
		sender.setOnClickListener(senderListener);
		reset.setOnClickListener(resetListener);
		tall.addTextChangedListener(textWatcher);
		weight.addTextChangedListener(textWatcher);

		mega.setOnClickListener(checkedListener);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			result.setText(defaultText);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	/**
	 * Just for the button sender
	 */
	private OnClickListener senderListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!mega.isChecked()) {
				// we get the size
				String t = tall.getText().toString();
				// we get the weight
				String p = weight.getText().toString();

				float tValue = Float.valueOf(t);

				// We check if the size is superior to 0
				if (tValue == 0)
					Toast.makeText(IMCActivity.this,
							"Vous ne pouvez calculer un IMC avec une taille égale à 0.",
							Toast.LENGTH_SHORT).show();
				else {
					float pValue = Float.valueOf(p);
					// In case of an another metric : cm
					if (group.getCheckedRadioButtonId() == R.id.radio2)
						tValue = tValue / 100;

					tValue = (float) Math.pow(tValue, 2);
					float imc = pValue / tValue;
					result.setText("Votre IMC est " + String.valueOf(imc));
				}
			} else
				result.setText(megaString);
		}
	};

	/**
	 * Listener of the button reset to 0
	 */
	private OnClickListener resetListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			weight.getText().clear();
			tall.getText().clear();
			result.setText(defaultText);
		}
	};

	private OnClickListener checkedListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!((CheckBox) v).isChecked()
					&& result.getText().equals(megaString))
				result.setText(defaultText);
		}
	};

}
