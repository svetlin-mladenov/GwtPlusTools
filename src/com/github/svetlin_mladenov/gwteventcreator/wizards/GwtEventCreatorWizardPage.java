package com.github.svetlin_mladenov.gwteventcreator.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
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
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class GwtEventCreatorWizardPage extends NewTypeWizardPage {
	private Text containerText;
	private String packageName;

	private Text eventNameText;

	private Button generateHasInterfaceButton;

	private Button generateSeparateGetTypeButton;

	private Button lazyCreateTypeButton;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public GwtEventCreatorWizardPage() {
		super(true, "wizardPage");
		setTitle("Gwt Event Creator");
		setDescription("This wizard creates the java source files required for a new Gwt Event.");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		
		final int nColumns= 4;
		
		Composite mainUiContainer = createUiMainContainer(parent, nColumns);
		
		createContainerControls(mainUiContainer, nColumns);
		createPackageControls(mainUiContainer, nColumns);
		createTypeNameControls(mainUiContainer, nColumns);
		
		createSeparator(mainUiContainer, nColumns);
		
		GridData optionsGD = new GridData(GridData.FILL_HORIZONTAL);
		optionsGD.horizontalSpan = 3;
		
		generateHasInterfaceButton = new Button(mainUiContainer, SWT.CHECK);
		generateHasInterfaceButton.setLayoutData(optionsGD);
		generateHasInterfaceButton.setText("Generated Has Interface");
		generateHasInterfaceButton.setSelection(true);
		
		generateSeparateGetTypeButton = new Button(mainUiContainer, SWT.CHECK);
		generateSeparateGetTypeButton.setLayoutData(optionsGD);
		generateSeparateGetTypeButton.setText("Generate Separate getType() method in Event class.");
		generateSeparateGetTypeButton.setSelection(true);
		
		lazyCreateTypeButton = new Button(mainUiContainer, SWT.CHECK);
		lazyCreateTypeButton.setLayoutData(optionsGD);
		lazyCreateTypeButton.setSelection(true);
		lazyCreateTypeButton.setText("Lazy Event's TYPE creation. (memory micro optimization)");
		
		setControl(mainUiContainer);
	}
	
	@Override
	protected String getTypeNameLabel() {
		//TODO externalize the string
		return "Event Name:";
	}

	private Composite createUiMainContainer(Composite parent, int nColumns) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = nColumns;
		layout.verticalSpacing = 9;
		
		return container;
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	public void initialize(IStructuredSelection selection) {
		IJavaElement jelem= getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		
		doStatusUpdate();
	}
	
	private void doStatusUpdate() {
		IStatus[] status = new IStatus[] {
				fContainerStatus,
				fPackageStatus,
				fTypeNameStatus
		};
		
		updateStatus(status);
	}
	
	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		
		doStatusUpdate();
	}
	
	public String getTargetFolderName() {
		return ensureSlash(getPackageFragmentRootText()) + getPackageName().replace('.', '/');
	}

	private String ensureSlash(String path) {
		if (!path.endsWith("/")) {
			return path + "/";
		} else {
			return path;
		}
	}

	public String getEventName() {
		return getTypeName();
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
		return getPackageFragment().getElementName();
	}
	
}