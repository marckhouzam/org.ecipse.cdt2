/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.cdt.internal.core.dom.parser.cpp;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTArrayDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTArrayModifier;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.parser.util.ArrayUtil;

/**
 * @author jcamelon
 */
public class CPPASTArrayDeclarator extends CPPASTDeclarator implements
        IASTArrayDeclarator {
    
    private IASTArrayModifier [] arrayMods = null;


    public IASTArrayModifier[] getArrayModifiers() {
        if( arrayMods == null ) return IASTArrayModifier.EMPTY_ARRAY;
        return (IASTArrayModifier[]) ArrayUtil.removeNulls( IASTArrayModifier.class, arrayMods );
    }

    public void addArrayModifier(IASTArrayModifier arrayModifier) {
        arrayMods = (IASTArrayModifier[]) ArrayUtil.append( IASTArrayModifier.class, arrayMods, arrayModifier );
    }
    
    protected boolean postAccept( ASTVisitor action ){
        IASTArrayModifier [] mods = getArrayModifiers();
        for ( int i = 0; i < mods.length; i++ ) {
            if( !mods[i].accept( action ) ) return false;
        }
        IASTInitializer initializer = getInitializer();
        if( initializer != null ) if( !initializer.accept( action ) ) return false;
        return true;
    }
}
