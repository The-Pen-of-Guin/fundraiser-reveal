import { useState } from "react";

type NodeType = "input" | "process" | "output";

interface NodeSettings {
  value?: string;
  threshold?: number;
  format?: string;
}

interface Node {
  id: string;
  name: string;
  type: NodeType;
  settings: NodeSettings;
}

const defaultSettings = (type: NodeType): NodeSettings => {
  switch (type) {
    case "input":
      return { value: "" };
    case "process":
      return { threshold: 0 };
    case "output":
      return { format: "json" };
  }
};

export default function App() {
  const [nodes, setNodes] = useState<Node[]>([]);

  const addNode = () => {
    const id = crypto.randomUUID();
    setNodes((prev) => [
      ...prev,
      { id, name: `Node ${prev.length + 1}`, type: "input", settings: defaultSettings("input") },
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
      <div className="max-w-3xl mx-auto">
        <div className="flex items-center justify-between mb-4">
          <h1 className="text-xl font-semibold text-gray-800">Animation Nodes</h1>
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
              className="flex items-center gap-3 bg-white border border-gray-200 rounded-lg p-3 shadow-sm"
            >
              <input
                value={node.name}
                onChange={(e) => updateNode(node.id, { name: e.target.value })}
                className="w-32 border border-gray-300 rounded px-2 py-1 text-sm"
              />

              <select
                value={node.type}
                onChange={(e) => changeType(node.id, e.target.value as NodeType)}
                className="border border-gray-300 rounded px-2 py-1 text-sm"
              >
                <option value="input">Input</option>
                <option value="process">Process</option>
                <option value="output">Output</option>
              </select>

              {node.type === "input" && (
                <input
                  placeholder="value"
                  value={node.settings.value ?? ""}
                  onChange={(e) => updateSettings(node.id, { value: e.target.value })}
                  className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                />
              )}

              {node.type === "process" && (
                <input
                  type="number"
                  placeholder="threshold"
                  value={node.settings.threshold ?? 0}
                  onChange={(e) => updateSettings(node.id, { threshold: Number(e.target.value) })}
                  className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                />
              )}

              {node.type === "output" && (
                <select
                  value={node.settings.format}
                  onChange={(e) => updateSettings(node.id, { format: e.target.value })}
                  className="flex-1 border border-gray-300 rounded px-2 py-1 text-sm"
                >
                  <option value="json">JSON</option>
                  <option value="csv">CSV</option>
                  <option value="text">Text</option>
                </select>
              )}

              <button
                onClick={() => removeNode(node.id)}
                className="text-red-500 hover:text-red-700 text-sm"
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
            className="px-4 py-2 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700"
          >Generate</button>
        </div>
      </div>
    </div>
  );
}
