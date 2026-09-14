import { useState } from "react";
import { SettingsButton } from "./SettingsButton";
import { OverlayMenu } from "./OverlayMenu";

type NodeType = "Set" | "Scramble" | "Countup";

interface NodeSettings {
  targetAmountCents?: number;
  startDelayMs?: number;
  durationMs?: number;
}

interface Node {
  id: string;
  type: NodeType;
  settings: NodeSettings;
}

const defaultSettings = (type: NodeType): NodeSettings => {
  var targetAmount = 0;
  var startDelay = 0;
  var duration = 0;
  switch (type) {
    case "Set":
      return { targetAmountCents: targetAmount, startDelayMs: startDelay };
    case "Scramble":
      return { targetAmountCents: targetAmount, startDelayMs: startDelay, durationMs: duration };
    case "Countup":
      return { targetAmountCents: targetAmount, startDelayMs: startDelay, durationMs: duration };
  }
};

async function generate() {
  await fetch("http://localhost:8080/api/v1/animation/play")
}

interface SetBackgroundColorRequest {
  r: number,
  g: number,
  b: number,
}

async function postBackgroundColor(payload: SetBackgroundColorRequest) {
  const response = await fetch("http://localhost:8080/api/v1/scene/background/color", {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error('HTTP error! Status: ${response.status}');
  }
}

interface SetTextColorRequest {
  r: number,
  g: number,
  b: number,
}

async function postTextColor(payload: SetTextColorRequest) {
  const response = await fetch("http://localhost:8080/api/v1/scene/text/color", {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error('HTTP error! Status: ${response.status}');
  }
}

interface SetFontRequest {
  font: string,
}

async function postFont(payload: SetFontRequest) {
  const response = await fetch("http://localhost:8080/api/v1/scene/text/font", {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error('HTTP error! Status: ${response.status}');
  }
}

export default function App() {
  const [nodes, setNodes] = useState<Node[]>([]);
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);

  const [backgroundColor, setBackgroundColor] = useState({ r: 255, g: 255, b:255 });
  const [textColor, setTextColor] = useState({ r: 0, g: 0, b:0 });

  const [font, setFont] = useState("Arial");

  const addNode = () => {
    const id = crypto.randomUUID();
    setNodes((prev) => [
      ...prev,
      { id, name: `Node ${prev.length + 1}`, type: "Set", settings: defaultSettings("Set") },
    ]);
  };

  const removeNode = (id: string) => {
    setNodes((prev) => prev.filter((n) => n.id !== id));
  };

  const updateNode = (id: string, patch: Partial<Node>) => {
    setNodes((prev) => prev.map((n) => (n.id === id ? { ...n, ...patch } : n)));
  };

  const updateSettings = (id: string, patch: Partial<NodeSettings>) => {
    setNodes((prev) =>
      prev.map((n) => (n.id === id ? { ...n, settings: { ...n.settings, ...patch } } : n))
    );
  };

  const changeType = (id: string, type: NodeType) => {
    updateNode(id, { type, settings: defaultSettings(type) });
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="min-w-fit mx-auto">
        {isSettingsOpen && <OverlayMenu 
          backgroundColor={backgroundColor}
          textColor={textColor}
          font={font}
          onBackgroundColorChange={(c) => {
            setBackgroundColor(c);
            postBackgroundColor(c);
          }}
          onTextColorChange={(c) => {
            setTextColor(c);
            postTextColor(c);
          }}
          onFontChange={(f) => {
            setFont(f);
            const request: SetFontRequest = {
              font: f,
            };
            postFont(request);
          }}
          onClose={() => setIsSettingsOpen(false)}
        />}
        <div className="flex items-center justify-between mb-4">
          <h1 className="text-xl font-semibold text-gray-800">Animation Nodes</h1>
          <SettingsButton
            className="ml-auto mr-4"
            onClick={() => setIsSettingsOpen(true)}
          />
          <button
            onClick={addNode}
            className="px-3 py-1.5 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700"
          >
            + Add Node
          </button>
        </div>

        <div className="space-y-2">
          {nodes.map((node) => (
            <div
              key={node.id}
              className="flex items-center gap-3 bg-white border border-gray-200 rounded-lg p-3 shadow-sm w-full"
            >
              <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                <label>Node Type</label>
                <select
                  value={node.type}
                  onChange={(e) => changeType(node.id, e.target.value as NodeType)}
                  className="border border-gray-300 rounded px-2 py-1 text-sm"
                >
                  <option value="Set">Set</option>
                  <option value="Scramble">Scramble</option>
                  <option value="Countup">Countup</option>
                </select>
              </div>

              {node.type === "Set" && (
                <>
                  <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                    <label>Target Amount (cents)</label>
                    <input
                      type="number"
                      placeholder="targetAmountCents"
                      value={node.settings.targetAmountCents ?? 0}
                      onChange={(e) => updateSettings(node.id, { targetAmountCents: Number(e.target.value) })}
                      className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                    />
                  </div>
                  <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                    <label>Start Delay (ms)</label>
                    <input
                      type="number"
                      placeholder="startDelayMs"
                      value={node.settings.startDelayMs ?? 0}
                      onChange={(e) => updateSettings(node.id, { startDelayMs: Number(e.target.value) })}
                      className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                    />
                  </div>
                </>
              )}

              {node.type === "Scramble" && (
                <>
                  <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                    <label>Target Amount (cents)</label>
                    <input
                      type="number"
                      placeholder="targetAmountCents"
                      value={node.settings.targetAmountCents ?? 0}
                      onChange={(e) => updateSettings(node.id, { targetAmountCents: Number(e.target.value) })}
                      className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                    />
                  </div>
                  <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                    <label>Start Delay (ms)</label>
                    <input
                      type="number"
                      placeholder="startDelayMs"
                      value={node.settings.startDelayMs ?? 0}
                      onChange={(e) => updateSettings(node.id, { startDelayMs: Number(e.target.value) })}
                      className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                    />
                  </div>
                  <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                    <label>Duration (ms)</label>
                    <input
                      type="number"
                      placeholder="durationMs"
                      value={node.settings.durationMs?? 0}
                      onChange={(e) => updateSettings(node.id, { durationMs: Number(e.target.value) })}
                      className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                    />
                  </div>
                </>
              )}

              {node.type === "Countup" && (
                <>
                  <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                    <label>Target Amount (cents)</label>
                    <input
                      type="number"
                      placeholder="targetAmountCents"
                      value={node.settings.targetAmountCents ?? 0}
                      onChange={(e) => updateSettings(node.id, { targetAmountCents: Number(e.target.value) })}
                      className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                    />
                  </div>
                  <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                    <label>Start Delay (ms)</label>
                    <input
                      type="number"
                      placeholder="startDelayMs"
                      value={node.settings.startDelayMs ?? 0}
                      onChange={(e) => updateSettings(node.id, { startDelayMs: Number(e.target.value) })}
                      className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                    />
                  </div>
                  <div style={{display: 'flex', flexDirection: 'column', marginBottom: '1rem'}}>
                    <label>Duration (ms)</label>
                    <input
                      type="number"
                      placeholder="durationMs"
                      value={node.settings.durationMs?? 0}
                      onChange={(e) => updateSettings(node.id, { durationMs: Number(e.target.value) })}
                      className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                    />
                  </div>
                </>
              )}

              <button
                onClick={() => removeNode(node.id)}
                className="ml-auto mr-4 text-red-500 hover:text-red-700 text-sm"
              >
                Remove
              </button>
            </div>
          ))}

          {nodes.length === 0 && (
            <p className="text-sm text-gray-400 text-center py-8">No nodes yet.</p>
          )}
        </div>
        <div className="flex justify-end">
          <button
            onClick={() => generate()}
            className="px-4 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700"
          >Generate</button>
        </div>
      </div>
    </div>
  );
}
