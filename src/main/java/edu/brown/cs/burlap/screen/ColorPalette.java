/*
 * Java Arcade Learning Environment (A.L.E) Agent
 *  Copyright (C) 2011-2012 Marc G. Bellemare <mgbellemare@ualberta.ca>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.brown.cs.burlap.screen;

import java.awt.*;

/** Defines a palette of colors. Up to 256 entries. 0 is always black.
 *
 * @author Marc G. Bellemare mgbellemare@ualberta.ca
 */
public abstract class ColorPalette {
    /** 256 colors in this palette */
    public static final int MAX_ENTRIES = 256;

    /** A map of edu.brown.cs.burlap.screen indices to RGB colors. */
    protected Color[] map;
    /** How many entries our map contains. */
    protected int numEntries;

    /** Create a color palette used to display the edu.brown.cs.burlap.screen. The currently available
     *   choices are NTSC (128 colors) and SECAM (8 colors).
     *
     * @param paletteName The name of the palette (NTSC or SECAM).
     * @return the {@link ColorPalette}
     */
    public static ColorPalette makePalette(String paletteName) {
        if (paletteName.equals("NTSC"))
            return new NTSCPalette();
        else if (paletteName.equals("SECAM"))
            return new SECAMPalette();
        else
            throw new IllegalArgumentException("Invalid palette: "+paletteName);
    }

    /**
     * Create a new map, with entry #0 being black.
     */
    public ColorPalette() {
        map = new Color[MAX_ENTRIES];
        // 0 is always black
        set(Color.BLACK, 0);
    }

    /** Returns how many entries are contained in this color map.
     * 
     * @return how many entries are contained in this color map.
     */
    public int numEntries() {
        return this.numEntries;
    }

    /**
     * Adds Color c at index i.
     * @param c Color
     * @param i index
     * @return the previous {@link Color}
     */
    public Color set(Color c, int i) {
        Color oldColor = map[i];

        map[i] = c;
        if (oldColor == null) numEntries++;

        return oldColor;
    }

    /** Returns the color indexed by i, possibly null.
     * 
     * @param i index of the color
     * @return the {@link Color}
     */
    public Color get(int i) {
        return map[i];
    }
    
    /** Returns whether palette index i has an associated color.
     * 
     * @param i the color index
     * @return true if there is a color for that index
     */
    public boolean hasEntry(int i) {
        return (map[i] != null);
    }    
}
