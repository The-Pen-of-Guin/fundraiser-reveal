type OverlayMenuProps = {
  onClose: () => void;
};

export function OverlayMenu({ onClose }: OverlayMenuProps) {
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
        style={{ background: "white", padding: "2rem", borderRadius: "8px" }}
        onClick={(e) => e.stopPropagation()} // prevent closing when clicking inside
      >
        <h2>Menu</h2>
        <ul>
          <li>Option 1</li>
          <li>Option 2</li>
          <li>Option 3</li>
        </ul>
        <button onClick={onClose}>Close</button>
      </div>
    </div>
  );
}
