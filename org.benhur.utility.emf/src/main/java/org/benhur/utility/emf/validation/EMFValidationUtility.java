package org.benhur.utility.emf.validation;

import java.util.List;

import org.benhur.utility.emf.UtilityEMFActivator;
import org.benhur.utility.emf.resource.EMFResourceUtility;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;

public class EMFValidationUtility
{
  public static IStatus isValidStructureAndConstraints(URI modelUri, IProgressMonitor monitor)
  {
    Resource resource = EMFResourceUtility.loadResource(modelUri, null, null,
                                                        EMFResourceUtility.getDefaultLoadSaveOptions());
    return isValidStructureAndConstraints(resource, monitor);
  }

  public static IStatus isValidStructureAndConstraints(Resource resource, IProgressMonitor monitor)
  {
    MultiStatus status = new MultiStatus(UtilityEMFActivator.PLUGIN_ID, IStatus.OK,
        "Validate structure and constraints.", null);
    status.add(validateStructure(resource, monitor));
    status.add(validateConstraints(resource, monitor));
    return status;
  }

  public static IStatus validateConstraints(URI modelUri, IProgressMonitor monitor)
  {
    Resource resource = EMFResourceUtility.loadResource(modelUri, null, null,
                                                        EMFResourceUtility.getDefaultLoadSaveOptions());
    return validateConstraints(resource, monitor);
  }

  public static IStatus validateConstraints(Resource resource, IProgressMonitor monitor)
  {
    MultiStatus status = new MultiStatus(UtilityEMFActivator.PLUGIN_ID, IStatus.OK, "Validate constraints", null);
    status.add(validateConstraints(resource.getContents(), monitor));
    return status;
  }

  public static IStatus validateConstraints(List<EObject> models, IProgressMonitor monitor)
  {
    IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance()
        .newValidator(EvaluationMode.BATCH);
    validator.setIncludeLiveConstraints(true);
    return validator.validate(models, monitor);
  }

  public static IStatus validateStructure(URI modelUri, IProgressMonitor monitor)
  {
    Resource resource = EMFResourceUtility.loadResource(modelUri, null, null,
                                                        EMFResourceUtility.getDefaultLoadSaveOptions());
    return validateStructure(resource, monitor);
  }

  public static IStatus validateStructure(Resource resource, IProgressMonitor monitor)
  {
    MultiStatus status = new MultiStatus(UtilityEMFActivator.PLUGIN_ID, IStatus.OK, "Validate structure", null);
    for (EObject object : resource.getContents())
    {
      status.add(validateStructure(object, monitor));
    }
    return status;
  }

  public static IStatus validateStructure(EObject model, IProgressMonitor monitor)
  {
    return BasicDiagnostic.toIStatus(Diagnostician.INSTANCE.validate(model));
  }
}
