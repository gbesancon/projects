package org.benhur.utility.emf.validation.custom;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

public interface IEMFModelCustomValidator
{
  List<IEMFModelElementCustomValidator<?>> getModelElementValidators();

  <T extends EObject> IStatus validate(T model, IProgressMonitor progressMonitor);
}
