package arenashooter.game.gameStates.editor.addItem;

import arenashooter.engine.ui.MultiUi;
import arenashooter.entities.Arena;
import arenashooter.game.gameStates.editor.ArenaEditor;
import arenashooter.game.gameStates.editor.Editor;

public enum ItemType {
	USABLE {
		@Override
		public MultiUi getEditor(Arena arena, Editor editor, ArenaEditor arenaEditor) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	GUN {
		@Override
		public MultiUi getEditor(Arena arena, Editor editor, ArenaEditor arenaEditor) {
			return new AddGunEditor(arena , editor , arenaEditor);
		}
	},
	MELEE {
		@Override
		public MultiUi getEditor(Arena arena, Editor editor, ArenaEditor arenaEditor) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	SHOTGUN {
		@Override
		public MultiUi getEditor(Arena arena, Editor editor, ArenaEditor arenaEditor) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	USABLETIME {
		@Override
		public MultiUi getEditor(Arena arena, Editor editor, ArenaEditor arenaEditor) {
			// TODO Auto-generated method stub
			return null;
		}

	};
	public abstract MultiUi getEditor(Arena arenaConstruction, Editor editor, ArenaEditor arenaEditor);
}
