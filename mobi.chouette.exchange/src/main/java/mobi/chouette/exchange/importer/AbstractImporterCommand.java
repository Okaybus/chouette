package mobi.chouette.exchange.importer;

import java.util.List;

import javax.naming.InitialContext;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.ChainCommand;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.ProcessingCommands;
import mobi.chouette.exchange.ProgressionCommand;
import mobi.chouette.exchange.report.ActionError;
import mobi.chouette.exchange.report.ActionReport;

public class AbstractImporterCommand implements Constant {

	protected enum Mode {
		line, stopareas
	};

	public boolean process(Context context, ProcessingCommands commands, ProgressionCommand progression,
			boolean continueProcesingOnError, Mode mode) throws Exception {
		boolean result = ERROR;
		InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
		ActionReport report = (ActionReport) context.get(REPORT);

		// initialisation
		List<? extends Command> preProcessingCommands = commands.getPreProcessingCommands(context, true);
		progression.initialize(context, preProcessingCommands.size() + 1);
		for (Command importCommand : preProcessingCommands) {
			result = importCommand.execute(context);
			if (!result) {
				report.setFailure(new ActionError(ActionError.CODE.NO_DATA_FOUND,"no data to import"));
				progression.execute(context);
				return ERROR;		
			}
			progression.execute(context);
		}

		if (mode.equals(Mode.line)) {
			// get lines info
			List<? extends Command> lineProcessingCommands = commands.getLineProcessingCommands(context, true);

			ChainCommand master = (ChainCommand) CommandFactory.create(initialContext, ChainCommand.class.getName());
			master.setIgnored(continueProcesingOnError);

			for (Command command : lineProcessingCommands) {
				master.add(progression);
				master.add(command);
			}
			progression.execute(context);

			if (lineProcessingCommands.size() > 0) {
				progression.start(context, lineProcessingCommands.size());
				if (master.execute(context) == ERROR && !continueProcesingOnError) {
					return ERROR;
				}
			}
		} else {
			// get stop info
			List<? extends Command> stopProcessingCommands = commands.getStopAreaProcessingCommands(context, true);
			progression.start(context, stopProcessingCommands.size());
			for (Command command : stopProcessingCommands) {
				result = command.execute(context);
				if (!result) {
					return ERROR;
				}
				progression.execute(context);
			}

		}
		// post processing
		List<? extends Command> postProcessingCommands = commands.getPostProcessingCommands(context, true);
		if (postProcessingCommands.isEmpty()) {
			progression.terminate(context, 1);
			progression.execute(context);
		} else {
			progression.terminate(context, postProcessingCommands.size());
			for (Command command : postProcessingCommands) {
				result = command.execute(context);
				if (!result) {
					return ERROR;
				}
				progression.execute(context);
			}
		}

		if (mode.equals(Mode.line) && report.getLines().size() == 0) {
			if (report.getFailure() == null)
				report.setFailure(new ActionError(ActionError.CODE.NO_DATA_FOUND, "no data"));
		}
		return result;
	}

}