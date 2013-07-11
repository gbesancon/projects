package org.benhur.utility.emf.validation.custom;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

public interface EMFModelElementCustomValidator<T extends EObject>
{
  IStatus validate(T modelElement, IProgressMonitor progressMonitor);
}
