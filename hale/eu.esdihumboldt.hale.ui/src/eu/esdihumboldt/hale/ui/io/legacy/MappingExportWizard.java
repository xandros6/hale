/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                  01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */
package eu.esdihumboldt.hale.ui.io.legacy;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;

import de.cs3d.util.logging.AGroupFactory;
import de.cs3d.util.logging.ALogger;
import de.cs3d.util.logging.ALoggerFactory;
import de.cs3d.util.logging.ATransaction;
import eu.esdihumboldt.commons.goml.align.Alignment;
import eu.esdihumboldt.cst.transformer.CellUtils;
import eu.esdihumboldt.hale.ui.internal.HALEUIPlugin;
import eu.esdihumboldt.hale.ui.internal.Messages;
import eu.esdihumboldt.hale.ui.io.legacy.mappingexport.MappingExportExtension;
import eu.esdihumboldt.hale.ui.io.legacy.mappingexport.MappingExportProvider;
import eu.esdihumboldt.hale.ui.io.legacy.mappingexport.MappingExportReport;
import eu.esdihumboldt.hale.ui.service.mapping.AlignmentService;
import eu.esdihumboldt.hale.ui.service.schema.SchemaService;
import eu.esdihumboldt.hale.ui.util.dialog.StackTraceErrorDialog;
import eu.esdihumboldt.specification.cst.align.ICell;

/**
 * This wizard is used to export the currently active mapping to an gOML file.
 * 
 * @author Thorsten Reitz 
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class MappingExportWizard 
	extends Wizard
	implements IExportWizard {
	
	private MappingExportWizardMainPage mainPage;
	
	private static final ALogger _log = ALoggerFactory.getLogger(MappingExportWizard.class);
	
	/**
	 * Default constructor
	 */
	public MappingExportWizard() {
		super();
		this.mainPage = new MappingExportWizardMainPage(
				Messages.MappingExportWizard_ExportMenu1, Messages.MappingExportWizard_ExportMenu2); //NON-NLS-1
		super.setWindowTitle(Messages.MappingExportWizard_WindowTitle); //NON-NLS-1
		super.setNeedsProgressMonitor(true);
	}
	
	/**
	 * @see IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		String path = this.mainPage.getResult();
		final String format = this.mainPage.getSelectedFormatName();
		final MappingExportProvider mef = MappingExportExtension.getExportProvider(format);
		String extension = MappingExportExtension.getRegisteredExportProviderInfo().get(format);
		extension = extension.substring(1);
		if (path != null) {
			if (!path.endsWith(extension)) {
				path = path + extension;
			}
			
			final String file = path;
			try {
				getContainer().run(true, false, new IRunnableWithProgress() {
					
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						AlignmentService alService = (AlignmentService) 
						PlatformUI.getWorkbench().getService(AlignmentService.class);
						Alignment al = alService.getAlignment();
				
						SchemaService schemaService = (SchemaService) PlatformUI.getWorkbench().getService(SchemaService.class);
						
						ATransaction trans = _log.begin("Export mapping to " + format); //$NON-NLS-1$
						try {
							//TODO instead give the monitor to the exporter? support for canceling?
							monitor.beginTask(Messages.MappingExportWizard_1, IProgressMonitor.UNKNOWN); //$NON-NLS-1$
							MappingExportReport report = mef.export(al, file, schemaService.getSourceSchemaElements(), 
									schemaService.getTargetSchemaElements()); //FIXME use schema objects instead of element lists
							if (report != null && !report.isEmpty()) {
								// handle report
								ATransaction reportTrans = _log.begin("Report"); //$NON-NLS-1$
								try {
									ATransaction failedTrans = _log.begin("Mapping cells that could not be exported"); //$NON-NLS-1$
									try {
										for (Entry<ICell, String> entry : report.getFailed().entrySet()) {
											_log.error(AGroupFactory.getGroup(entry.getValue()),
													CellUtils.asString(entry.getKey()));
										}
									} finally {
										failedTrans.end();
									}
									ATransaction warningsTrans = _log.begin("Problems with exported mapping cells"); //$NON-NLS-1$
									try {
										for (Entry<ICell, String> entry : report.getWarnings().entrySet()) {
											_log.warn(AGroupFactory.getGroup(entry.getValue()),
													CellUtils.asString(entry.getKey()));
										}
									} finally {
										warningsTrans.end();
									}
								} finally {
									reportTrans.end();
								}
								
								int failed = report.getFailed().size();
								int problems = report.getWarnings().size();
								int all = al.getMap().size();
								final String message = MessageFormat.format(Messages.MappingExportWizard_0, failed, problems, all, (all-failed)); //$NON-NLS-1$
								
								PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
									
									@Override
									public void run() {
										StackTraceErrorDialog dialog = new StackTraceErrorDialog(
												Display.getCurrent().getActiveShell(), 
												null, 
												null, 
												new Status(IStatus.WARNING, HALEUIPlugin.PLUGIN_ID, message), 
												IStatus.OK | IStatus.INFO | IStatus.WARNING | IStatus.ERROR);
										dialog.setShowErrorLogLink(true);
										dialog.open();
									}
								});
								
								final MappingExportReport report2 = report;
								Runnable r = new Runnable(){
									@Override
									public void run(){
										//XXX deactivated for now
//										ReportServiceImpl reportService = (ReportServiceImpl)PlatformUI.getWorkbench().getService(ReportService.class);
//										reportService.addReport(report2);
									}
								};

								PlatformUI.getWorkbench().getDisplay().asyncExec(r);
							}
							else {
								_log.userInfo(Messages.MappingExportWizard_3); 
							}
						} catch (Throwable e) {
							String message = Messages.MappingExportWizard_SaveFailed;
							_log.userError(message + " " + e.getMessage(), e); //$NON-NLS-1$
						} finally {
							monitor.done();
							trans.end();
						}
					}
				});
			} catch (Exception e) {
				_log.userError(Messages.MappingExportWizard_2, e); 
			}
			
		}
		return true;
	}

	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// do nothing
	}

	/**
	 * @see IWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPage(this.mainPage);
	}

	/**
	 * @see IWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return this.mainPage.isPageComplete();
	}

}