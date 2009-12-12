package openjchart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.Map;

import openjchart.DrawableConstants.Location;
import openjchart.DrawableConstants.Orientation;
import openjchart.data.DataSource;
import openjchart.plots.Label;
import openjchart.util.GraphicsUtils;
import openjchart.util.Insets2D;
import openjchart.util.SettingChangeEvent;
import openjchart.util.Settings;
import openjchart.util.SettingsListener;
import openjchart.util.SettingsStorage;

public abstract class Legend extends DrawableContainer implements SettingsStorage, SettingsListener {
	public static final String KEY_BACKGROUND = "legend.background";
	public static final String KEY_BORDER = "legend.border";
	public static final String KEY_ORIENTATION = "legend.orientation";
	public static final String KEY_GAP = "legend.gap";

	private final Settings settings;

	private final Map<DataSource, Drawable> components;

	protected class Item extends DrawableContainer {
		private final DataSource data;
		private final Drawable symbol;
		private final Label label;

		public Item(final DataSource data, final String labelText) {
			super(new EdgeLayout(10.0, 0.0));
			this.data = data;

			symbol = new AbstractDrawable() {
				@Override
				public void draw(Graphics2D g2d) {
					drawSymbol(g2d, this, Item.this.data);
				}

				@Override
				public Dimension2D getPreferredSize() {
					Dimension2D size = super.getPreferredSize();
					size.setSize(20.0, 20.0);
					return size;
				}
			};
			label = new Label(labelText);
			label.setSetting(Label.KEY_ALIGNMENT_X, 0.0);
			label.setSetting(Label.KEY_ALIGNMENT_Y, 0.5);

			add(symbol, Location.WEST);
			add(label, Location.CENTER);
		}

		@Override
		public Dimension2D getPreferredSize() {
			return getLayout().getPreferredSize(this);
		}

		public DataSource getData() {
			return data;
		}
	}

	public Legend() {
		components = new HashMap<DataSource, Drawable>();
		setInsets(new Insets2D.Double(10.0));

		settings = new Settings(this);
		setSettingDefault(KEY_BACKGROUND, Color.WHITE);
		setSettingDefault(KEY_BORDER, new BasicStroke(1f));
		setSettingDefault(KEY_ORIENTATION, Orientation.VERTICAL);
		setSettingDefault(KEY_GAP, new openjchart.util.Dimension2D.Double(20.0, 5.0));
	}

	@Override
	public void draw(Graphics2D g2d) {
		drawBackground(g2d);
		drawBorder(g2d);

		AffineTransform txOrig = g2d.getTransform();
		g2d.translate(getX(), getY());
		drawComponents(g2d);
		g2d.setTransform(txOrig);
	}

	protected void drawBackground(Graphics2D g2d) {
		Paint bg = getSetting(KEY_BACKGROUND);
		if (bg != null) {
			GraphicsUtils.fillPaintedShape(g2d, getBounds(), bg, null);
		}
	}

	protected void drawBorder(Graphics2D g2d) {
		Stroke borderStroke = getSetting(KEY_BORDER);
		if (borderStroke != null) {
			Stroke strokeOld = g2d.getStroke();
			g2d.setStroke(borderStroke);
			g2d.draw(getBounds());
			g2d.setStroke(strokeOld);
		}
	}

	protected abstract void drawSymbol(Graphics2D g2d, Drawable symbol, DataSource data);

	public void add(DataSource data) {
		Item item = new Item(data, data.toString());
		add(item);
		components.put(data, item);
	}

	public void remove(DataSource series) {
		Drawable removeItem = components.get(series);
		if (removeItem != null) {
			remove(removeItem);
		}
		components.remove(series);
	}

	protected void notifyDataChanged() {
		layout();
	}

	@Override
	public <T> T getSetting(String key) {
		return settings.get(key);
	}

	@Override
	public <T> void removeSetting(String key) {
		settings.remove(key);
	}

	@Override
	public <T> void removeSettingDefault(String key) {
		settings.removeDefault(key);
	}

	@Override
	public <T> void setSetting(String key, T value) {
		settings.set(key, value);
	}

	@Override
	public <T> void setSettingDefault(String key, T value) {
		settings.setDefault(key, value);
	}

	@Override
	public void settingChanged(SettingChangeEvent event) {
		String key = event.getKey();
		if (KEY_ORIENTATION.equals(key) || KEY_GAP.equals(key)) {
			Orientation orientation = getSetting(KEY_ORIENTATION);
			Dimension2D gap = getSetting(KEY_GAP);
			Layout layout = new StackedLayout(orientation, gap);
			setLayout(layout);
		}
	}

}