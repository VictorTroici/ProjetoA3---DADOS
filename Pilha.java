package pilha;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



public class Main {
	public static void main(String[] args) throws FileNotFoundException {
        List<PilhaGrupo> grupos = new ArrayList<>();
        List<String> resultados = new ArrayList<>();
        
        

        try (BufferedReader br = new BufferedReader(new FileReader("entrada.txt"))) {
            String linha;
            
           
                       
                 
            
            boolean lista1Open = true;
            boolean lista2Open = false;
            boolean lista3Open = false;

            List<String> fila1 = new ArrayList<>();
            List<String> fila2 = new ArrayList<>();
            List<String> fila3 = new ArrayList<>();
            
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split("\\s+");
                String comando = partes[0].toLowerCase();
                
                
                switch (comando) {
                    case "grupo:":
                        PilhaGrupo grupo = new PilhaGrupo();
                        for (int i = 1; i < partes.length; i++) {
                            grupo.adicionarPessoa(partes[i]);
                        }
                        grupos.add(grupo);
                        break;

                    case "existe:":
                        String pessoaABuscar = partes[1].toLowerCase();
                        boolean existe = false;
                        for (PilhaGrupo g : grupos) {
                            if (g.pessoaExiste(pessoaABuscar)) {
                                resultados.add("[" + pessoaABuscar + "] existe!");
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            resultados.add("[" + pessoaABuscar + "] Nao existe!");
                        }
                        break;

                    case "conhece:":
                        String pessoa1 = partes[1].toLowerCase();
                        String pessoa2 = partes[2].toLowerCase();
                        boolean encontrou = false;
                        for (PilhaGrupo g : grupos) {
                            if (g.pessoaExiste(pessoa1) && g.pessoaExiste(pessoa2)) {
                                resultados.add("[" + pessoa1 + "] conhece [" + pessoa2 + "]");
                                encontrou = true;
                                break;
                            }
                        }
                        if (!encontrou) {
                            resultados.add("[" + pessoa1 + "] Nao conhece [" + pessoa2 + "]");
                        }
                        break;
                        
                    case "criaroutrafila:":                    	
                    	
                    	if (lista1Open == true && lista2Open == false) {
                    		lista2Open = true;
        	        		resultados.add("Fila 2 aberta!");
        	        		break;
        	        	}else if(lista1Open == true && lista2Open == true) {
        	        		lista3Open = true; 
        	        		resultados.add("Fila 3 aberta!");
        	        		break;
        	        	}else {
        	        		resultados.add("numero de filas limite atingido");
        	        		break;
        	        	}
                    	
                    	
                    case "chegarpessoa:":
						

						int tamanhoFila1 = fila1.size(), tamanhoFila2 = fila2.size(), tamanhoFila3 = fila3.size();
                    	String pessoaAChegar = partes[1].toLowerCase();
						
						// a lista abaixo guarda pessoas que ela conhece pertencente a todos os grupos, 
						// pois a pessoa pode estar em mais de 1 grupo.
						// elementos da lista mudam conforme a pessoa nova que chega
						List<String> pessoasQueConhece = new ArrayList<>();

						for (int i_teste = 0; i_teste < grupos.size(); i_teste++) {
							PilhaGrupo pegarGruposTeste = (PilhaGrupo) grupos.get(i_teste);
							List <String> listaDePessoasTeste = pegarGruposTeste.getGrupo(pessoaAChegar);
							if (listaDePessoasTeste.contains(pessoaAChegar)) {
								pessoasQueConhece.addAll(listaDePessoasTeste);
							}
						}
						
						resultados.add(pessoaAChegar + " chegou");

						// verificando se a pessoa já está na fila
						if(fila1.contains(pessoaAChegar) || 
							fila2.contains(pessoaAChegar) || 
							fila3.contains(pessoaAChegar)) {

							resultados.add("pessoa já na fila");

						// verifica se nas filas existem alguém que a pessoa conhece
						} else if (!Collections.disjoint(pessoasQueConhece, fila1) || 
								   !Collections.disjoint(pessoasQueConhece, fila2) || 
								   !Collections.disjoint(pessoasQueConhece, fila3)) {
							
							int index1 = -1, index2 = -1, index3 = -1;

							// salva o index da última pessoa que conhece em cada fila
							// se não conhecer, o index continua negativo
							for (String pessoa : fila1) {
								if (pessoasQueConhece.contains(pessoa)) {
									index1 = fila1.indexOf(pessoa) + 1;
								}
							}
							for (String pessoa : fila2) {
								if (pessoasQueConhece.contains(pessoa)) {
									index2 = fila2.indexOf(pessoa) + 1;
								}
							}
							for (String pessoa : fila3) {
								if (pessoasQueConhece.contains(pessoa)) {
									index3 = fila3.indexOf(pessoa) + 1;
								}
							}

							// adiciona pessoa na fila, cortando fila ou não
							// se apenas fila1 estar aberta
							if (!lista2Open && !lista3Open) {
								// se conhecer alguém da fila
								if (index1 > 0) {
									fila1.add(index1, pessoaAChegar);
								} else {
									fila1.add(pessoaAChegar);
								}
							// se fila1 e fila2 estarem abertas
							} else if (!lista3Open) {
								// se conhecer alguém da fila 1 e 2
								if (index1 > 0 && index2 > 0) {
									if (index1 <= index2) {
										fila1.add(index1, pessoaAChegar);
									} else {
										fila2.add(index2, pessoaAChegar);
									}
								// se conhecer apenas da fila 1
								} else if (index1 > 0) {
									if (index1 <= tamanhoFila2) {
										fila1.add(index1, pessoaAChegar);
									} else {
										fila2.add(pessoaAChegar);
									}
								// se conhecer apenas da fila 2
								} else if (index2 > 0) {
									if (index2 <= tamanhoFila1) {
										fila2.add(index2, pessoaAChegar);
									} else {
										fila1.add(pessoaAChegar);
									}
								// se não conhecer ninguém
								} else {
									if (tamanhoFila1 <= tamanhoFila2) {
										fila1.add(pessoaAChegar);
									} else {
										fila2.add(pessoaAChegar);
									}
								}
							// se todas as filas estarem abertas
							} else {
								// se conhecer pessoa da fila 1, 2 e 3
								if (index1 > 0 && index2 > 0 && index3 > 0) {
									if (index1 <= index2) {
										if (index1 <= index3) {
											fila1.add(index1, pessoaAChegar);
										} else {
											fila3.add(index3, pessoaAChegar);
										}
									} else if (index2 <= index3) {
										fila2.add(index2, pessoaAChegar);
									} else {
										fila3.add(index3, pessoaAChegar);
									}
								// se conhecer apenas da fila 1 e 2	
								} else if (index1 > 0 && index2 > 0) {
									if (tamanhoFila3 <= index1 && tamanhoFila3 <= index2) {
										fila3.add(pessoaAChegar);
									} else if (index1 <= index2) {
										fila1.add(index1, pessoaAChegar);
									} else {
										fila2.add(index2, pessoaAChegar);
									}
								// se conhecer apenas da fila 1 e 3
								} else if (index1 > 0 && index3 > 0) {
									if (tamanhoFila2 <= index1 && tamanhoFila2 <= index3) {
										fila2.add(pessoaAChegar);
									} else if (index1 <= index3) {
										fila1.add(index1, pessoaAChegar);
									} else {
										fila3.add(index3, pessoaAChegar);
									}
								// se conhecer apenas da fila 2 e 3
								} else if (index2 > 0 && index3 > 0) {
									if (tamanhoFila1 <= index2 && tamanhoFila1 <= index3) {
										fila1.add(pessoaAChegar);
									} else if (index2 <= index3) {
										fila2.add(index2, pessoaAChegar);
									} else {
										fila3.add(index3, pessoaAChegar);
									}
								// se conhecer apenas da fila 1
								} else if (index1 > 0) {
									if (tamanhoFila2 <= tamanhoFila3) {
										if (index1 <= tamanhoFila2){
											fila1.add(index1, pessoaAChegar);
										} else {
											fila2.add(pessoaAChegar);
										}
									} else {
										if (index1 <= tamanhoFila3) {
											fila1.add(index1, pessoaAChegar);
										} else {
											fila3.add(pessoaAChegar);
										}
									}
								// se conhecer apenas da fila 2
								} else if (index2 > 0) {
									if (tamanhoFila1 <= tamanhoFila3) {
										if (index2 <= tamanhoFila1) {
											fila2.add(index2, pessoaAChegar);
										} else {
											fila1.add(pessoaAChegar);
										}
									} else {
										if (index2 <= tamanhoFila3) {
											fila2.add(index2, pessoaAChegar);
										} else {
											fila3.add(pessoaAChegar);
										}
									}
								// se conhecer apenas da fila 3
								} else if (index3 > 0) {
									if (tamanhoFila1 <= tamanhoFila2) {
										if (index3 <= tamanhoFila1) {
											fila3.add(index3, pessoaAChegar);
										} else {
											fila1.add(pessoaAChegar);
										}
									} else {
										if (index3 <= tamanhoFila2) {
											fila3.add(index3, pessoaAChegar);
										} else {
											fila2.add(pessoaAChegar);
										}
									}
								}
							}
						// se a pessoa não conhece ninguém nas filas
						} else {
							// se apenas fila 1 estar aberta
							if (!lista2Open && !lista3Open) {
								fila1.add(pessoaAChegar);
							// se lista fila 1 e 2 estarem abertas
							} else if (!lista3Open) {
								if (tamanhoFila1 <= tamanhoFila2) {
									fila1.add(pessoaAChegar);
								} else {
									fila2.add(pessoaAChegar);
								}
							// se todas as filas estarem abertas
							} else {
								if (tamanhoFila1 <= tamanhoFila2) {
									if (tamanhoFila1 <= tamanhoFila3) {
										fila1.add(pessoaAChegar);
									} else {
										fila3.add(pessoaAChegar);
									}
								} else if (tamanhoFila2 <= tamanhoFila3) {
									fila2.add(pessoaAChegar);
								} else {
								fila3.add(pessoaAChegar);
								}
							}
						}

                    case "imprimir:":
                    	resultados.add("Imprimir filas atuais");
                    	if (lista1Open == true && lista2Open == false && lista3Open == false) {
                    		
                    		resultados.add("#Fila 1 " + fila1.toString());
                    	
                    	}else if(lista1Open == true && lista2Open == true && lista3Open == false) {
                    		
                    		resultados.add("#Fila 1 " + fila1.toString());
                    		resultados.add("#Fila 2 " + fila2.toString());
                    		
                    	}else if(lista1Open == true && lista2Open == true && lista3Open == true) {
                    	
                    		resultados.add("#Fila 1 " + fila1.toString());
                    		resultados.add("#Fila 2 " + fila2.toString());
                    		resultados.add("#Fila 3 " + fila3.toString());
                    	
                    	}
                    	break;
                    	
                    case "desistirpessoa:":
                    	
                    	String pessoaADesistir = partes[1].toLowerCase();
                    	String filaremove = partes[2].toLowerCase();
                    	
                    	switch(filaremove) {
                    	case "fila1":
                    		fila1.remove(pessoaADesistir);
                    		break;
                    		
                    	case "fila2":
                    		fila2.remove(pessoaADesistir);
                    		break;
                    		
                    	case "fila3":
                    		fila3.remove(pessoaADesistir);
                    		break;
                    		
                    	default: 
                    		resultados.add("Pessoa ou fila não existe");
                    	
                    	}
                    	
                    	break;
                    	
                    case "atenderfila:":
                    	if (fila1.size() > 0)
                    		fila1.remove(0);
						
                    	if (fila2.size() > 0)
                    		fila2.remove(0);

						if (fila3.size() > 0)
                    		fila3.remove(0);
						
						resultados.add("Filas atendidas");
                    	
                    	break;
                    	
                    	
                    
                    	
                }
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mostrar todos os resultados
        System.out.println("Resultados:");
        for (String resultado : resultados) {
            System.out.println(resultado);
        }

    }

}

	
	
