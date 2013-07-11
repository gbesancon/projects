package org.benhur.utility.emf.validation.custom.impl;

import java.util.ArrayList;
import java.util.List;

import org.benhur.utility.emf.validation.custom.EMFModelCustomValidator;
import org.benhur.utility.emf.validation.custom.EMFModelElementCustomValidator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

public class EMFModelCustomValidatorImpl implements EMFModelCustomValidator
{
  protected List<EMFModelElementCustomValidator> modelElementValidators = new ArrayList<EMFModelElementCustomValidator>();

  @Override
  public List<EMFModelElementCustomValidator> getModelElementValidators()
  {
    return modelElementValidators;
  }

  @Override
  public <T extends EObject> IStatus validate(T model, IProgressMonitor progressMonitor)
  {
    return null;
  }
}
