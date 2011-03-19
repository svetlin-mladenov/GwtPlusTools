package com.github.svetlin_mladenov.gwteventcreator.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.jdt.core.IJavaElement;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class GwtEventCreatorWizardPage extends WizardPage {
	private Text containerText;
	private String packageName;

	private Text eventNameText;

	private ISelection selection;

	private Button generateHasInterfaceButton;

	private Button generateSeparateGetTypeButton;

	private Button lazyCreateTypeButton;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public GwtEventCreatorWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Gwt Event Creator");
		setDescription("This wizard creates the java source files required for a new Gwt Event.");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Container:");

		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText("&File name:");

		eventNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		eventNameText.setLayoutData(gd);
		eventNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		GridData optionsGD = new GridData(GridData.FILL_HORIZONTAL);
		optionsGD.horizontalSpan = 3;
		
		generateHasInterfaceButton = new Button(container, SWT.CHECK);
		generateHasInterfaceButton.setLayoutData(optionsGD);
		generateHasInterfaceButton.setText("Generated Has Interface");
		generateHasInterfaceButton.setSelection(true);
		
		generateSeparateGetTypeButton = new Button(container, SWT.CHECK);
		generateSeparateGetTypeButton.setLayoutData(optionsGD);
		generateSeparateGetTypeButton.setText("Generate Separate getType() method in Event class.");
		generateSeparateGetTypeButton.setSelection(true);
		
		lazyCreateTypeButton = new Button(container, SWT.CHECK);
		lazyCreateTypeButton.setLayoutData(optionsGD);
		lazyCreateTypeButton.setSelection(true);
		lazyCreateTypeButton.setText("Lazy Event's TYPE creation. (memory micro optimization)");
		
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IJavaElement) {
				obj = ((IJavaElement)obj).getResource();
			}
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer) {
					container = (IContainer) obj;
				}
				else {
					container = ((IResource) obj).getParent();
				}
				setContainerText(container.getFullPath());
			}
		}
	}
	
	private void setContainerText(IPath path) {
		containerText.setText(path.toString());
		packageName = EventStructsNames.extractPackageName(path);
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				setContainerText((Path) result[0]);
			}
		}
		
		
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getEventName();

		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("Event name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getEventName() {
		return eventNameText.getText();
	}
	
	public boolean generateHasInterface() {
		return generateHasInterfaceButton.getSelection();
	}
	
	public boolean generateSeperateGetTypeMethod() {
		return generateSeparateGetTypeButton.getSelection();
	}
	
	public boolean lazyCreateEventType() {
		return lazyCreateTypeButton.getSelection();
	}
	
	public String getPackageName() {
		return packageName;
	}
	
}