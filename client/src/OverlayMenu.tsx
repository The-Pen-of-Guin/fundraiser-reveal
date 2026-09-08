type OverlayMenuProps = {
  backgroundColor: { r: number; g: number; b: number };
  onBackgroundColorChange: (color: { r: number, g: number, b: number }) => void;
  textColor: { r: number; g: number; b: number };
  onTextColorChange: (color: { r: number, g: number, b: number }) => void;
  font: string;
  onFontChange: (font: string) => void;
  onClose: () => void;
};

export function OverlayMenu({
  backgroundColor,
  onBackgroundColorChange,
  textColor,
  onTextColorChange,
  font,
  onFontChange,
  onClose
}: OverlayMenuProps) {
  const fontOptions = [
    "Arial",
    "Roboto",
    "Times New Roman",
  ];

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
	<div style={{ display: "flex", gap: "1rem", alignItems: "center", marginTop: "1rem", marginLeft: "1rem" }}>
	  <label className="font-semibold text-gray-800">Background Color</label>
	  <input
	    type="color"
	    value={rgbToHex(backgroundColor.r, backgroundColor.g, backgroundColor.b)}
	    onChange={(e) =>
	      onBackgroundColorChange(hexToRgb(e.target.value))}
	  />
	</div>
	<div style={{ display: "flex", gap: "1rem", alignItems: "center", marginTop: "1rem", marginLeft: "1rem" }}>
	  <label className="font-semibold text-gray-800">Text Color</label>
	  <input
	    type="color"
	    value={rgbToHex(textColor.r, textColor.g, textColor.b)}
	    onChange={(e) => onTextColorChange(hexToRgb(e.target.value))}
	  />
	</div>
	<div style={{ display: "flex", gap: "1rem", alignItems: "center", marginTop: "1rem", marginLeft: "1rem" }}>
	  <label className="font-semibold text-gray-800">Text Font</label>
	  <select value={font} onChange={(e) => onFontChange(e.target.value)}>
	    {fontOptions.map((f) => (
	      <option key={f} value={f} style={{ fontFamily: f }}>
	        {f}
	      </option>
	    ))}
	  </select>
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
