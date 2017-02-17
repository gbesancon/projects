package org.benhur.utility.emf.validation.custom.impl;

import java.util.ArrayList;
import java.util.List;

import org.benhur.utility.emf.UtilityEMFActivator;
import org.benhur.utility.emf.validation.custom.IEMFModelCustomValidator;
import org.benhur.utility.emf.validation.custom.IEMFModelElementCustomValidator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;

public abstract class AEMFModelCustomValidator implements IEMFModelCustomValidator
{
  private List<IEMFModelElementCustomValidator<?>> modelElementValidators = new ArrayList<IEMFModelElementCustomValidator<?>>();

  public AEMFModelCustomValidator()
  {
    declareModelElementValidator(modelElementValidators);
  }

  protected abstract void declareModelElementValidator(List<IEMFModelElementCustomValidator<?>> modelElementValidators);

  @SuppressWarnings("unchecked")
  public <T extends EObject> IStatus validate(T model, IProgressMonitor progressMonitor)
  {
    IStatus status = null;
    IEMFModelElementCustomValidator<T> modelElementValidator = null;
    for (IEMFModelElementCustomValidator<?> aEMFModelElementCustomValidator : modelElementValidators)
    {
      if (aEMFModelElementCustomValidator.getModelElementClass().isInstance(model))
      {
        modelElementValidator = (IEMFModelElementCustomValidator<T>) aEMFModelElementCustomValidator;
      }
    }

    if (modelElementValidator != null)
    {
      status = modelElementValidator.validate(model, progressMonitor);
    }
    else
    {
      status = new Status(IStatus.INFO, UtilityEMFActivator.PLUGIN_ID,
          String.format("%s %s is valid", model.eClass().getName(), model.toString()));
    }
    return status;
  }
}
