import { useEffect, useState } from 'react'
import './App.css'

const API = 'http://localhost:8080'

type Estado = { principal: number; enVuelo: number; dlq: number }

export default function App() {
  const [estado, setEstado] = useState<Estado>({ principal: 0, enVuelo: 0, dlq: 0 })
  const [mensaje, setMensaje] = useState('')

  async function crearOrden(cliente: string) {
    const orden = { id: crypto.randomUUID().slice(0, 8), cliente, monto: 10000 }
    const res = await fetch(`${API}/ordenes`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(orden),
    })
    setMensaje(await res.text())
  }

  useEffect(() => {
    const t = setInterval(async () => {
      try {
        const res = await fetch(`${API}/estado`)
        setEstado(await res.json())
      } catch {
        // backend no disponible
      }
    }, 3000)

    return () => clearInterval(t)
  }, [])

  return (
    <main>
      <h1>Procesador de órdenes</h1>

      {['Cliente 1', 'Cliente 2', 'Cliente 3'].map((cliente) => (
        <button key={cliente} type="button" onClick={() => crearOrden(cliente)}>
          Orden {cliente}
        </button>
      ))}

      <p>{mensaje}</p>

      <section className="tarjetas">
        <div className="tarjeta ok">
          <h2>{estado.principal}</h2>
          <span>En cola</span>
        </div>
        <div className="tarjeta aviso">
          <h2>{estado.enVuelo}</h2>
          <span>En proceso</span>
        </div>
        <div className={`tarjeta ${estado.dlq > 0 ? 'alerta' : 'ok'}`}>
          <h2>{estado.dlq}</h2>
          <span>En DLQ</span>
        </div>
      </section>

      {estado.dlq > 0 && <p className="banner">⚠ Hay órdenes fallidas</p>}
    </main>
  )
}
