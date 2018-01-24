public class _WindowLineups extends Tab {
	public _WindowLineups(_ModuleLineupList lineupList) {
		super("Combinations", createToolbar(), lineupList);
	}

	private static Toolbar createToolbar() {
		Toolbar toolbar = new Toolbar();
		return toolbar;
	}
}
