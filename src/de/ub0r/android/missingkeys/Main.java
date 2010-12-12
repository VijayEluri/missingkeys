package de.ub0r.android.missingkeys;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener,
		OnLongClickListener {

	/** Dialog: about. */
	private static final int DIALOG_ABOUT = 0;

	/** Prefix ins {@link SharedPreferences}. */
	private static final String PREFS_PREFIX = "btn_";

	/** Buttons pid for R.id. */
	private final HashMap<Integer, String> btns = new HashMap<Integer, String>();

	/**
	 * Load a {@link Button}.
	 * 
	 * @param id
	 *            R.id
	 * @param pid
	 *            id in {@link SharedPreferences}
	 */
	private void loadBtn(final int id, final String pid,
			final SharedPreferences p) {
		final Button v = (Button) this.findViewById(id);
		v.setOnClickListener(this);
		v.setOnLongClickListener(this);
		final String rpid = PREFS_PREFIX + pid;
		final String s = p.getString(rpid, "");
		if (s.length() > 0) {
			v.setText(s);
		}
		this.btns.put(id, rpid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		final SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(this);
		this.loadBtn(R.id.btn_1_1, "1_1", p);
		this.loadBtn(R.id.btn_1_2, "1_2", p);
		this.loadBtn(R.id.btn_1_3, "1_3", p);
		this.loadBtn(R.id.btn_1_4, "1_4", p);
		this.loadBtn(R.id.btn_1_5, "1_5", p);
		this.loadBtn(R.id.btn_1_6, "1_6", p);
		this.loadBtn(R.id.btn_2_1, "2_1", p);
		this.loadBtn(R.id.btn_2_2, "2_2", p);
		this.loadBtn(R.id.btn_2_3, "2_3", p);
		this.loadBtn(R.id.btn_2_4, "2_4", p);
		this.loadBtn(R.id.btn_2_5, "2_5", p);
		this.loadBtn(R.id.btn_2_6, "2_6", p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(final View v) {
		if (v instanceof Button) {
			final Button btn = (Button) v;
			final ClipboardManager cbmgr = (ClipboardManager) this
					.getSystemService(CLIPBOARD_SERVICE);
			cbmgr.setText(btn.getText());
			this.finish();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onLongClick(final View v) {
		if (v instanceof Button) {
			final Button btn = (Button) v;
			final AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle(R.string.edit_);
			b.setCancelable(true);
			final EditText et = new EditText(this);
			et.setText(btn.getText());
			b.setView(et);
			b.setNegativeButton(android.R.string.cancel, null);
			b.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int which) {
							final String text = et.getText().toString();
							btn.setText(text);
							PreferenceManager.getDefaultSharedPreferences(
									Main.this).edit().putString(
									Main.this.btns.get(v.getId()), text)
									.commit();
						}
					});
			b.show();
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_about: // start about dialog
			this.showDialog(DIALOG_ABOUT);
			return true;
		case R.id.item_more:
			try {
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("market://search?q=pub:\"Felix Bechstein\"")));
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, "missing market application",
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final Dialog onCreateDialog(final int id) {
		Dialog d;
		switch (id) {
		case DIALOG_ABOUT:
			d = new Dialog(this);
			d.setContentView(R.layout.about);
			d.setTitle(this.getString(R.string.about_) + " v"
					+ this.getString(R.string.app_version));
			return d;
		default:
			return null;
		}
	}
}