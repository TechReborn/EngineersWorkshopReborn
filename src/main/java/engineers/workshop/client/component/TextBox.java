package engineers.workshop.client.component;

import engineers.workshop.client.GuiBase;
import net.minecraft.util.ChatUtil;

public class TextBox {

	private static final int SRC_X = 68;
	private static final int SRC_Y = 98;
	private static final int WIDTH = 82;
	private static final int HEIGHT = 12;
	private static final float MULTIPLIER = 0.7F;
	private static final int TEXT_X = 3;
	private static final int TEXT_Y = 5;

	private int x;
	private int y;
	private String text;
	private int cursor;
	private boolean updatedCursor = true;
	private int cursorPosition;

	public TextBox(int x, int y) {
		this.x = x;
		this.y = y;
		text = "";
	}

	public void draw(GuiBase gui, int mX, int mY) {
		if (isVisible()) {
			gui.prepare();
			gui.drawRect(x, y, SRC_X, SRC_Y, WIDTH, HEIGHT);
			gui.drawString(text, x + TEXT_X, y + TEXT_Y, MULTIPLIER, 0x1E1E1E);
			gui.drawCursor(x + getCursorPosition(gui) + 2, y + 1, 10, 1F, 0xFF909090);
		}
	}

	private void addText(GuiBase gui, String str) {
		String newText = text.substring(0, cursor) + str + text.substring(cursor);

		if (gui.getStringWidth(newText) * MULTIPLIER <= WIDTH) {
			text = newText;
			moveCursor(gui, str.length());
			textChanged();
		}
	}

	private void deleteText(GuiBase gui, int direction) {
		if (cursor + direction >= 0 && cursor + direction <= text.length()) {
			if (direction > 0) {
				text = text.substring(0, cursor) + text.substring(cursor + 1);
			} else {
				text = text.substring(0, cursor - 1) + text.substring(cursor);
				moveCursor(gui, direction);
			}
			textChanged();
		}
	}

	protected void textChanged() {

	}

	private void moveCursor(GuiBase gui, int steps) {
		cursor += steps;
		updateCursor();
	}

	public void onKeyStroke(GuiBase gui, int c, int k) {
		if (isVisible()) {
			if (k == 203) {
				moveCursor(gui, -1);
			} else if (k == 205) {
				moveCursor(gui, 1);
			} else if (k == 14) {
				deleteText(gui, -1);
			} else if (k == 211) {
				deleteText(gui, 1);
			} else {
				//TODO fix me
				//addText(gui, ChatUtil.stripTextFormat(Character.toString(c)));
			}
		}
	}

	public void updateCursor() {
		if (cursor < 0) {
			cursor = 0;
		} else if (cursor > text.length()) {
			cursor = text.length();
		}

		updatedCursor = true;
	}

	public int getCursorPosition(GuiBase gui) {
		if (updatedCursor) {
			cursorPosition = (int) (gui.getStringWidth(text.substring(0, cursor)) * MULTIPLIER);
			updatedCursor = false;
		}

		return cursorPosition;
	}

	public String getText() {
		return text;
	}

	public boolean isVisible() {
		return true;
	}
}
