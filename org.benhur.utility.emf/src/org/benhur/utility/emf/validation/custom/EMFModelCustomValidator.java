package org.benhur.utility.emf.validation.custom;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

public interface EMFModelCustomValidator
{
  List<EMFModelElementCustomValidator> getModelElementValidators();

  <T extends EObject> IStatus validate(T model, IProgressMonitor progressMonitor);
}
