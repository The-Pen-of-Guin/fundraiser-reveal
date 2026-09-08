import { useState } from "react";

type OverlayMenuProps = {
  onClose: () => void;
};

export function OverlayMenu({ onClose }: OverlayMenuProps) {
  const [backgroundColor, setBackgroundColor] = useState({ r: 255, g: 255, b:255 });

  const rgbToHex = (r: number, g: number, b: number) =>
    "#" + [r, g, b].map((v) => v.toString(16).padStart(2, "0")).join("");

  const hexToRgb = (hex: string) => ({
    r: parseInt(hex.slice(1, 3), 16),
    g: parseInt(hex.slice(3, 5), 16),
    b: parseInt(hex.slice(5, 7), 16),
  });

  return (
    <div
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        width: "100%",
        height: "100%",
        background: "rgba(0,0,0,0.6)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: 1000,
      }}
      onClick={onClose} // click outside menu closes it
    >
      <div
        style={{
	  background: "white",
	  padding: "2rem",
	  borderRadius: "8px",
	  width: "80%",
	  maxWidth: "900px",
	  height: "80%",
	  overflowY: "auto",
	  display: "flex",
	  flexDirection: "column",
	}}
        onClick={(e) => e.stopPropagation()} // prevent closing when clicking inside
      >
        <h1 className="text-xl font-semibold text-gray-800">Settings Menu</h1>
	<div style={{ display: "flex", gap: "1rem", alignItems: "center", marginTop: "1rem" }}>
	  <label className="font-semibold text-gray-800">Background Color</label>
	  <input
	    type="color"
	    value={rgbToHex(backgroundColor.r, backgroundColor.g, backgroundColor.b)}
	    onChange={(e) => setBackgroundColor(hexToRgb(e.target.value))}
	  />
	</div>
	<div style={{ textAlign: "center", marginTop: "auto" }}>
          <button onClick={onClose}
	    className="px-4 py-1.5 bg-blue-600 text-white rounded-md text-l hover:bg-blue-700"
	  >Close</button>
	</div>
      </div>
    </div>
  );
}
