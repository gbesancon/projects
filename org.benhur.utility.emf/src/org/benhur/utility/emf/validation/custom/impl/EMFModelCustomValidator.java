package org.benhur.utility.emf.validation.custom.impl;

import java.util.ArrayList;
import java.util.List;

import org.benhur.utility.emf.validation.custom.IEMFModelCustomValidator;
import org.benhur.utility.emf.validation.custom.IEMFModelElementCustomValidator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

public class EMFModelCustomValidator implements IEMFModelCustomValidator
{
  protected List<IEMFModelElementCustomValidator<?>> modelElementValidators = new ArrayList<IEMFModelElementCustomValidator<?>>();

  @Override
  public List<IEMFModelElementCustomValidator<?>> getModelElementValidators()
  {
    return modelElementValidators;
  }

  @Override
  public <T extends EObject> IStatus validate(T model, IProgressMonitor progressMonitor)
  {
    return null;
  }
}
