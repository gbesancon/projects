package org.benhur.utility.emf.validation.custom;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

public interface IEMFModelElementCustomValidator<T extends EObject>
{
  Class<T> getModelElementClass();

  IStatus validate(T modelElement, IProgressMonitor progressMonitor);
}
